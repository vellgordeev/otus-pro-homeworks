package ru.gordeev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gordeev.annotations.*;
import ru.gordeev.helpers.MethodPriorityComparator;
import ru.gordeev.helpers.Reporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationProccessor {
    private final Logger logger;
    private Object instance;
    private final Reporter resultedReport;
    private Method beforeSuiteMethod;
    private List<Method> beforeMethods;
    private List<Method> testMethods;
    private List<Method> afterMethods;
    private Method afterSuiteMethod;


    public AnnotationProccessor() {
        this.resultedReport = new Reporter();
        beforeMethods = new ArrayList<>();
        afterMethods = new ArrayList<>();
        testMethods = new ArrayList<>();
        this.logger = LogManager.getLogger(AnnotationProccessor.class);
    }

    public void run(Class<?> testClass) {
        if (testClass.isAnnotationPresent(Disabled.class)) {
            logger.info(String.format("%s is disabled", testClass));
            return;
        }

        resultedReport.clearResults();
        try {
            instance = testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.error(String.format("Error creating instance of test class: %s", e.getMessage()));
            return;
        }

        beforeMethods.clear();
        afterMethods.clear();
        testMethods.clear();

        parseAnnotations(testClass);
        testMethods.sort(new MethodPriorityComparator());
        executeTestMethods();
    }

    private void parseAnnotations(Class<?> testClass) {
        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Disabled.class)) {
                resultedReport.addSkipped();
                continue;
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuiteMethod != null) {
                    throw new IllegalArgumentException("More than one @BeforeSuite method found");
                }
                beforeSuiteMethod = method;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuiteMethod != null) {
                    throw new IllegalArgumentException("More than one @AfterSuite method found");
                }
                afterSuiteMethod = method;
            }
            if (method.isAnnotationPresent(Test.class)) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                    continue;
                }
                if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                    continue;
                }
                testMethods.add(method);
            }
        }
    }

    private void executeTestMethods() {
        if (testMethods.isEmpty()) {
            logger.error("There is no tests to run");
        }
        invokeSuiteMethod(beforeSuiteMethod);

        for (Method testMethod : testMethods) {
            invokeBeforeOrAfterMethods(beforeMethods);
            executeTestMethodWithExceptionCheck(testMethod);
            invokeBeforeOrAfterMethods(afterMethods);
        }

        invokeSuiteMethod(afterSuiteMethod);
        resultedReport.printResults();
    }

    private void invokeBeforeOrAfterMethods(List<Method> methods) {
        if (methods.isEmpty()) return;
        for (Method method : methods) {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                logger.error(String.format("Error during @Before/@After method execution: %s", e.getMessage()));
            }
        }
    }

    private void invokeSuiteMethod(Method suiteMethod) {
        if (suiteMethod != null) {
            try {
                suiteMethod.invoke(instance);
            } catch (Exception e) {
                logger.error(String.format("Error during suite method execution: %s", e.getMessage()));
            }
        }
    }

    private void executeTestMethodWithExceptionCheck(Method testMethod) {
        boolean isExceptionExpected = testMethod.isAnnotationPresent(ThrowsException.class);
        Class<? extends Throwable> expectedException = isExceptionExpected ? testMethod.getAnnotation(ThrowsException.class).value() : null;

        try {
            testMethod.invoke(instance);
            if (isExceptionExpected) {
                logger.error(String.format("Test %s failed: expected %s to be thrown",
                        testMethod.getName(), expectedException.getName()));
                resultedReport.addFailed();
            } else {
                resultedReport.addSuccessful();
            }
        } catch (InvocationTargetException e) {
            Throwable thrownException = e.getTargetException();
            if (isExceptionExpected && expectedException.isInstance(thrownException)) {
                resultedReport.addSuccessful();
            } else {
                String expectedExceptionName = isExceptionExpected ? expectedException.getName() : "none";
                logger.error(String.format("Test %s failed: expected %s, but caught %s",
                        testMethod.getName(),
                        expectedExceptionName,
                        thrownException.getClass().getName()));
                resultedReport.addFailed();
            }
        } catch (Exception e) {
            logger.error(String.format("Unexpected error during execution of test %s: %s",
                    testMethod.getName(), e.getMessage()));
            resultedReport.addFailed();
        }
    }

}
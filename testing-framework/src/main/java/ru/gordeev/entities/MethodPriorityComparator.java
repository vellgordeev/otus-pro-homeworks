package ru.gordeev.entities;

import ru.gordeev.annotations.Test;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodPriorityComparator implements Comparator<Method> {

    @Override
    public int compare(Method m1, Method m2) {
        Test t1 = m1.getAnnotation(Test.class);
        Test t2 = m2.getAnnotation(Test.class);

        return Integer.compare(t1.priority(), t2.priority());
    }
}

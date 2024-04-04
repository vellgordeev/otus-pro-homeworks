import ru.gordeev.annotations.*;

public class Tests {

    @BeforeSuite
    public void setUp() {
        System.out.println("set up");
    }

    @Before
    public void testBeforeShouldBeIgnored() {
        System.out.println("this test should be ignored");
    }

    @Before
    @Test
    public void testBefore() {
        System.out.println("before every test");
    }

    @After
    @Test
    public void testAfter() {
        System.out.println("after every test");
    }

    @Test(priority = 1)
    public void testOne() {
        System.out.println("test 1");
    }

    @Test(priority = 1)
    public void testSecondOne() {
        System.out.println("test 1");
    }

    @Disabled
    @Test(priority = 1)
    public void testDisabled() {
        System.out.println("disabled?");
    }

    @Disabled
    @Test(priority = 2)
    public void testTwo() {
        System.out.println("test 2");
    }

    @Test(priority = 3)
    public void testThree() {
        System.out.println("test 3");
    }

    @Test(priority = 4)
    public void testFourFailed() {
        throw new RuntimeException("test 4");
    }

    @Test(priority = 5)
    @ThrowsException(ArithmeticException.class)
    public void testArithmeticException() {
        System.out.println("the big bang happened");
        int result = 1 / 0;
    }

    @Test(priority = 6)
    @ThrowsException(NullPointerException.class)
    public void testWrongExceptionInAnnotation() {
        System.out.println("here is the wrong exception");
        int result = 1 / 0;
    }

    @Test(priority = 7)
    @ThrowsException(ArithmeticException.class)
    public void testExpectedExceptionWithout() {
        System.out.println("there should be an exception here, but there isn't one");
    }

    @AfterSuite
    public void tearDown() {
        System.out.println("tear down");
    }
}

package ru.otus.hw05.testFramework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
    private final Class<?> testClass;
    private final List<Method> after = new ArrayList<>();
    private final List<Method> before = new ArrayList<>();
    private final List<Method> tests = new ArrayList<>();

    public TestClass(String className) throws ClassNotFoundException {
        testClass = Class.forName(className);
    }

    public TestResult runTests() throws Exception {
        readAnnotations();
        validate();
        TestResult result = new TestResult();
        for (Method testMethod : tests) {
            result.countOne(runTest(testMethod));
        }
        return result;
    }

    private boolean runTest(Method testMethod) throws Exception {
        Object testObject = testClass.getConstructor().newInstance();

        boolean beforePassed = invokeBefore(testObject);
        boolean testPassed = false;
        if (beforePassed) {
            testPassed = invokeTest(testObject, testMethod);
        }
        boolean afterPassed = invokeAfter(testObject);

        return beforePassed && testPassed && afterPassed;
    }

    private boolean invokeBefore(Object testObject) {
        try {
            for (Method method : before) {
                method.invoke(testObject);
            }
            return true;
        } catch (InvocationTargetException ex) {
            System.out.println(testClass.getName() + " @Before method error: " + ex.getTargetException());
        } catch (Exception ex) {
            System.out.println(testClass.getName() + " @Before method error: " + ex);
        }
        return false;
    }

    private boolean invokeAfter(Object testObject) {
        try {
            for (Method method : after) {
                method.invoke(testObject);
            }
            return true;
        } catch (InvocationTargetException ex) {
            System.out.println(testClass.getName() + " @After method error: " + ex.getTargetException());
        } catch (Exception ex) {
            System.out.println(testClass.getName() + " @After method error: " + ex);
        }
        return false;
    }

    private boolean invokeTest(Object testObject, Method testMethod) {
        String methodName = testClass.getName() + "." + testMethod.getName();
        try {
            testMethod.invoke(testObject);
            System.out.println(methodName + " passed");
            return true;
        } catch (InvocationTargetException ex) {
            System.out.println(methodName + " failed: \r\n" + ex.getTargetException());
        } catch (Exception ex) {
            System.out.println(methodName + " failed: \r\n" + ex);
        }
        return false;
    }

    private void validate() {
        if (after.size() > 1)
            throw new RuntimeException("Too many @After methods in " + testClass.getName());
        if (before.size() > 1)
            throw new RuntimeException("Too many @Before methods in " + testClass.getName());
        if (tests.size() < 1)
            throw new RuntimeException("No @Test methods found in " + testClass.getName());
    }

    private void readAnnotations() {
        for (Method method : testClass.getMethods()) {
            if (method.getDeclaredAnnotation(After.class) != null) {
                after.add(method);
            }
            if (method.getDeclaredAnnotation(Before.class) != null) {
                before.add(method);
            }
            if (method.getDeclaredAnnotation(Test.class) != null) {
                tests.add(method);
            }
        }
    }
}

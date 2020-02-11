package ru.otus.hw05.testFramework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRun {
    public static void main(String... args) throws Exception {
        if (args == null || args.length == 0) {
            throw new RuntimeException("Please specify test classes");
        }

        TestResult total = new TestResult();
        for (var testName : args) {
            total.append(runSingleTest(testName));
        }
        System.out.println(String.format("Total tests passed %s of %s", total.getPassed(), total.getCount()));
        System.exit(total.getFailed() == 0 ? 0 : 1);
    }

    private static TestResult runSingleTest(String testName) throws Exception {
        TestResult result = new TestResult();
        Class<?> testClass = Class.forName(testName);
        List<Method> after = new ArrayList<>();
        List<Method> before = new ArrayList<>();
        List<Method> tests = new ArrayList<>();
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

        if (after.size() > 1)
            throw new RuntimeException("Too many @After methods in " + testName);
        if (before.size() > 1)
            throw new RuntimeException("Too many @Before methods in " + testName);
        if (tests.size() < 1)
            throw new RuntimeException("No @Test methods found in " + testName);

        for (Method testMethod : tests) {
            Object testObject = testClass.getConstructor().newInstance();
            for (Method method : before) {
                method.invoke(testObject);
            }
            try {
                testMethod.invoke(testObject);
                System.out.println(testName + "." + testMethod.getName() + " passed");
                result.countOne(true);
            } catch (InvocationTargetException ex) {
                System.out.println(testName + "." + testMethod.getName() + " failed: \r\n" + ex.getTargetException().toString());
                result.countOne(false);
            } catch (Exception ex) {
                System.out.println(testName + "." + testMethod.getName() + " failed: \r\n" + ex.toString());
                result.countOne(false);
            }
            for (Method method : after) {
                method.invoke(testObject);
            }
        }
        return result;
    }
}

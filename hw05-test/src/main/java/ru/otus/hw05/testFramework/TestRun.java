package ru.otus.hw05.testFramework;

public class TestRun {
    public static void main(String... args) throws Exception {
        var total = new TestRun().run(args);
        System.exit(total.getFailed() == 0 ? 0 : 1);
    }

    public TestResult run(String... tests) throws Exception {
        if (tests == null || tests.length == 0) {
            throw new RuntimeException("Please specify test classes");
        }
        TestResult total = new TestResult();
        for (var testName : tests) {
            var test = new TestClass(testName);
            total.append(test.runTests());
        }
        System.out.println(String.format("Total tests passed %s of %s", total.getPassed(), total.getCount()));
        return total;
    }
}

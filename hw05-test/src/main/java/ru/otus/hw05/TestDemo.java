package ru.otus.hw05;

import ru.otus.hw05.testFramework.TestRun;

public class TestDemo {
    public static void main(String... args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[] {"ru.otus.hw05.TestClass1", "ru.otus.hw05.TestClass2"};
        }
        var total = new TestRun().run(args);
        System.exit(total.getFailed() == 0 ? 0 : 1);
    }
}

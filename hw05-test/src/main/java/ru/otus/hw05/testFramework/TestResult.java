package ru.otus.hw05.testFramework;

public class TestResult {
    private int failed;
    private int passed;

    public void append(TestResult tr) {
        failed += tr.failed;
        passed += tr.passed;
    }

    public void countOne(boolean success) {
        if (success)
            this.passed++;
        else
            this.failed++;
    }

    public int getFailed() {
        return failed;
    }

    public int getPassed() {
        return passed;
    }

    public int getCount() {
        return passed + failed;
    }
}

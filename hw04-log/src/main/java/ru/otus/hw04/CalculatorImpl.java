package ru.otus.hw04;

public class CalculatorImpl implements Calculator {
    @Override
    @MyLog
    public int calculation(int param) {
        return param;
    }

    @Override
    @MyLog
    public int calculation(String param1, int param2, Object param3) {
        return param2;
    }

    @Override
    public int methodWithoutLog() {
        return 0;
    }
}

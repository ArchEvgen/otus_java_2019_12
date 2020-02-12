package ru.otus.hw04;

public class CalculatorImplWithoutLog implements Calculator {
    @Override
    public int calculation(int param) {
        return param;
    }

    @Override
    public int calculation(String param1, int param2, Object param3) {
        return param2;
    }

    @Override
    public int methodWithoutLog() {
        return 0;
    }
}

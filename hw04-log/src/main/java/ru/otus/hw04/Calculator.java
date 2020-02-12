package ru.otus.hw04;

public interface Calculator {
    int calculation(int param);

    int calculation(String param1, int param2, Object param3);

    int methodWithoutLog();
}

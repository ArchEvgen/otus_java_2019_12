package ru.otus.hw05;

import ru.otus.hw05.testFramework.Test;

public class TestClass2 {
    @Test
    public void good() {
        assert 1 + 1 == 2;
    }

    @Test
    public void bad() {
        assert 1 + 1 == 3;
    }
}

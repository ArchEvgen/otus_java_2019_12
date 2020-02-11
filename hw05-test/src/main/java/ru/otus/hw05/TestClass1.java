package ru.otus.hw05;

import ru.otus.hw05.testFramework.After;
import ru.otus.hw05.testFramework.Before;
import ru.otus.hw05.testFramework.Test;

public class TestClass1 {
    private int x;

    @Before
    public void setUp() {
        x = 10;
    }

    @After
    public void close() {
        System.out.println("TestClass1 closed, x=" + x);
    }

    @Test
    public void testIntMultiply() {
        x *= 3;
        assert x == 30;
    }

    @Test
    public void testIntAdd() {
        x += 3;
        assert x == 13;
    }
}

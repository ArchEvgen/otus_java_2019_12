package ru.otus.hw04;

public class LogDemo {
    public static void main(String... args) throws NoSuchMethodException {
        var calculator = Ioc.createCalculator();
        calculator.calculation(42);
        calculator.calculation("должно логироваться", 2, 3.0);
        calculator.calculation("", 0, null);
        calculator.methodWithoutLog();

        calculator = Ioc.createCalculatorWithoutLog();
        calculator.calculation("не должно логироваться!", 0, null);
    }
}

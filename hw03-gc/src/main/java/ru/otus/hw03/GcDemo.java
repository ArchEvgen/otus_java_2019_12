package ru.otus.hw03;

public class GcDemo {
    public static void main(String... args) throws Exception {
        GcMonitoring gcMonitoring = new GcMonitoring();
        gcMonitoring.switchOn();
        Benchmark benchmark = new Benchmark();
        benchmark.run();
    }
}

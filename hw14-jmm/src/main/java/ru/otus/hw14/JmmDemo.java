package ru.otus.hw14;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;

public class JmmDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-----------------testPrintOrder-----------------");
        testPrintOrder();
        System.out.println("-----------------testPerfomance-----------------");
        testAtomicDomination();
    }

    public static void testPrintOrder() throws InterruptedException {
        var max = 10;
        IntConsumer printFunc = (i) -> System.out.println(Thread.currentThread().getName() + ": " + i);
        System.out.println("<AtomicCounter>");
        Counter counter = new AtomicCounter(printFunc);
        counter.run(max);
        System.out.println("</AtomicCounter>");
        System.out.println("<SyncCounter>");
        counter = new SyncCounter(printFunc);
        counter.run(max);
        System.out.println("<SyncCounter>");
    }

    public static void testAtomicDomination() throws InterruptedException {
        IntConsumer printFunc = (i) -> {};
        testPerfomance(new AtomicCounter(printFunc));
        testPerfomance(new SyncCounter(printFunc));
    }

    public static void testPerfomance(Counter counter) throws InterruptedException {
        var max = 1000000;
        var startAt = Instant.now();
        counter.run(max);
        var finAt = Instant.now();
        var elapsed = Duration.between(startAt, finAt).toNanos() / 1000000000.0;
        System.out.println(String.format("%s counts to %s at %s", counter.getClass().getSimpleName(), max, elapsed));
    }
}

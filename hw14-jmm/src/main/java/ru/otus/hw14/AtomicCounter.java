package ru.otus.hw14;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;

public class AtomicCounter implements Counter {
    private final AtomicBoolean thread1Turn = new AtomicBoolean();
    private int max;
    private int counter1;
    private int counter2;
    private volatile int increment = 1;
    private final IntConsumer printFunc;

    public AtomicCounter(IntConsumer printFunc) {
        this.printFunc = printFunc;
    }

    public void run(int max) throws InterruptedException {
        this.max = max;
        increment = 1;
        counter1 = 0;
        counter2 = 0;
        thread1Turn.set(true);
        var thread1 = new Thread(this::step1, "thread1");
        var thread2 = new Thread(this::step2, "thread2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread1.join();
    }

    private void step1() {
        while (counter1 >= 0) {
            if (thread1Turn.get()) {
                printFunc.accept(counter1);
                counter1+=increment;
                thread1Turn.set(false);
            }
        }
        thread1Turn.set(false);
    }

    private void step2() {
        while (counter2 >= 0) {
            if (!thread1Turn.get()) {
                printFunc.accept(counter2);
                counter2+=increment;
                thread1Turn.set(true);
                if (counter2 == max) {
                    increment = -1;
                }
            }
        }
        thread1Turn.set(true);
    }
}

package ru.otus.hw14;

import java.util.function.IntConsumer;

public class SyncCounter implements Counter {
    private final IntConsumer printFunc;
    private final Object syncObj = new Object();
    private boolean thread1Turn;
    private int max;
    private int counter1;
    private int counter2;
    private int increment;

    public SyncCounter(IntConsumer printFunc) {
        this.printFunc = printFunc;
    }

    public void run(int max) throws InterruptedException {
        this.max = max;
        increment = 1;
        counter1 = 0;
        counter2 = 0;
        thread1Turn = true;

        var thread1 = new Thread(this::step1, "thread1");
        var thread2 = new Thread(this::step2, "thread2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread1.join();
    }

    private void swapTurn() {
        thread1Turn = !thread1Turn;
    }

    private void step1() {
        while (counter1 >= 0) {
            synchronized (syncObj) {
                if (thread1Turn) {
                    printFunc.accept(counter1);
                    counter1 += increment;
                    swapTurn();
                }
            }
        }
    }

    private void step2() {
        while (counter2 >= 0) {
            synchronized (syncObj) {
                if (!thread1Turn) {
                    printFunc.accept(counter2);
                    counter2 += increment;
                    if (counter2 == max && counter1 == max) {
                        increment = -1;
                    }
                    swapTurn();
                }
            }
        }
    }
}

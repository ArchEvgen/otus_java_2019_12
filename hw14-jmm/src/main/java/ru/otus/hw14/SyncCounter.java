package ru.otus.hw14;

import java.util.function.IntConsumer;

public class SyncCounter implements Counter {
    private boolean thread1Turn;
    private int max;
    private int counter1;
    private int counter2;
    private int increment;
    private final IntConsumer printFunc;

    public SyncCounter(boolean printResult) {
        if (printResult) {
            printFunc = (i) -> System.out.println(Thread.currentThread().getName() + ": " + i);
        } else {
            printFunc = (i) -> {};
        }
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

    private synchronized boolean isThread1Turn() {
        return thread1Turn;
    }

    private synchronized void swapTurn() {
        thread1Turn = !thread1Turn;
    }

    private void step1() {
        while (counter1 >= 0) {
            if (isThread1Turn()) {
                printFunc.accept(counter1);
                counter1 += increment;
                swapTurn();
            }
        }
    }

    private void step2() {
        while (counter2 >= 0) {
            if (!isThread1Turn()) {
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

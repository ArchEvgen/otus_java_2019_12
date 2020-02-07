package ru.otus.hw03;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

public class Benchmark {
    private int sleepMills = 100;
    private LinkedList<Instant> list = new LinkedList<>();
    private int batchSize = 100000;
    private int removeSize = 90000;
    private long iterationCounter = 0;
    private long sumLatency = 0;
    private long maxLatency = 0;
    private Instant runAt;
    private long uptime;

    public void run() throws InterruptedException {
        runAt = Instant.now();
        while (true) {
            addBatch();
            cleanup();
            Thread.sleep(sleepMills);
        }
    }

    private void addBatch() {
        list.add(Instant.now());
        for (int i = 0; i < batchSize; i++) {
            var lastTime = list.getLast();
            var newTime = Instant.now();
            uptime = Duration.between(runAt, newTime).toMillis();
            var latency = Duration.between(lastTime, newTime).toMillis();
            list.add(newTime);
            sumLatency += latency;
            maxLatency = Math.max(maxLatency, latency);
            iterationCounter++;
        }
    }

    private void cleanup() {
        for (int i = 0; i < removeSize; i++) {
            list.removeFirst();
        }
    }
}

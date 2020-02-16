package ru.otus.hw06;

public interface Cash {
    Cash add(Cash cash);

    Cash subtract(Cash cash);

    int getBills(Denomination denomination);

    int getSum();
}

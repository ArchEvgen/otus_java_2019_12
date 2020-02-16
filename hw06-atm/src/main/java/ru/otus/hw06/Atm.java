package ru.otus.hw06;

public interface Atm {
    void push(Cash cash);

    Cash pull(int sum);

    int getCashSum();
}

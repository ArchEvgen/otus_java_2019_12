package ru.otus.hw07;

public interface Atm {
    void push(Cash cash);

    Cash pull(int sum);
}

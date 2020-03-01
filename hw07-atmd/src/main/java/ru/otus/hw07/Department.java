package ru.otus.hw07;

import java.util.UUID;
import java.util.function.Consumer;

public interface Department {
    void addAtm(AtmUnit atm);
    int getDepartmentCashSum();
    int getAtmCashSum(UUID atmId);
    void encashAll();
    void executeCommand(Consumer<AtmUnit> command);
}

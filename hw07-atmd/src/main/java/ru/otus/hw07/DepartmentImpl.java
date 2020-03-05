package ru.otus.hw07;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DepartmentImpl implements Department {
    private final Map<UUID, AtmUnit> atmUnits = new HashMap<>();

    @Override
    public void addAtm(AtmUnit atm) {
        atmUnits.put(atm.getId(), atm);
    }

    @Override
    public int getDepartmentCashSum() {
        return atmUnits.values().stream().map(ServiceAtm::getCashSum).reduce(Integer::sum).orElse(0);
    }

    @Override
    public int getAtmCashSum(UUID atmId) {
        return atmUnits.get(atmId).getCashSum();
    }

    @Override
    public void encashAll() {
        executeCommand(AtmUnit::encash);
    }

    @Override
    public void executeCommand(Consumer<AtmUnit> command) {
        for (var atm : atmUnits.values()) {
            command.accept(atm);
        }
    }
}

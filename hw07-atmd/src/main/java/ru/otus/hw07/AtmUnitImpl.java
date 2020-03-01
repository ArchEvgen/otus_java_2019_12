package ru.otus.hw07;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class AtmUnitImpl implements AtmUnit {
    private final UUID id;
    private final AtmImpl atm;
    private Cash initialSum;
    private final Set<Consumer<AtmUnit>> onCashChangedListeners = new HashSet<>();

    public AtmUnitImpl(UUID id, AtmImpl atm, Cash initialSum) {
        this.atm = atm;
        this.id = id;
        this.initialSum = initialSum;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Cash encash() {
        var result = atm.encash(this.initialSum);
        onCashChangedHandler();
        return result;
    }

    @Override
    public void push(Cash cash) {
        atm.push(cash);
        onCashChangedHandler();
    }

    @Override
    public Cash pull(int sum) {
        var result = atm.pull(sum);
        onCashChangedHandler();
        return result;
    }

    @Override
    public int getCashSum() {
        return atm.getCashSum();
    }

    @Override
    public Cash encash(Cash newInitialSum) {
        this.initialSum = newInitialSum;
        return this.encash();
    }

    protected void onCashChangedHandler() {
        for (var listener : this.onCashChangedListeners) {
            listener.accept(this);
        }
    }

    @Override
    public void addOnCashChangedHandler(Consumer<AtmUnit> listener) {
        this.onCashChangedListeners.add(listener);
    }

    @Override
    public void removeOnCashChangedHandler(Consumer<AtmUnit> listener) {
        this.onCashChangedListeners.remove(listener);
    }
}

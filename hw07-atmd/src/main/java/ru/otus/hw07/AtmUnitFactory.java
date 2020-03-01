package ru.otus.hw07;

import java.util.UUID;

public class AtmUnitFactory {
    private Cash initial;

    public AtmUnitFactory(Cash initial) {
        this.initial = initial;
    }

    public AtmUnitFactory() {
        this(CashImpl.builder().build());
    }

    public AtmUnit createAtm() {
        return new AtmUnitImpl(UUID.randomUUID(), new AtmImpl(initial), initial);
    }
}

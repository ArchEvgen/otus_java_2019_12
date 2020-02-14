package ru.otus.hw06;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CashImpl implements Cash {
    private final Map<Denomination, Integer> bills;

    private CashImpl(Map<Denomination, Integer> bills) {
        this.bills = new HashMap<>(bills);
    }

    @Override
    public Cash add(Cash cash) {
        var result = CashImpl.builder();
        Denomination.SORTED_BY_VALUE_DESC.forEach(d -> result.with(d, cash.getBills(d) + this.getBills(d)));
        return result.build();
    }

    @Override
    public Cash subtract(Cash cash) {
        var result = CashImpl.builder();
        Denomination.SORTED_BY_VALUE_DESC.forEach(d -> {
            Integer count = getBills(d);
            Integer diff = cash.getBills(d);
            if (count < diff) {
                throw new CashOperationException("Not enough bills of " + d);
            }
            result.with(d, count - diff);
        });
        return result.build();
    }

    @Override
    public int getBills(Denomination denomination) {
        return bills.getOrDefault(denomination, 0);
    }

    @Override
    public int getSum() {
        return bills.entrySet().stream()
                .map(x -> x.getKey().getValue() * x.getValue())
                .reduce(Integer::sum).orElse(0);
    }

    public static CashBuilder builder() {
        return new CashBuilder();
    }

    public static class CashBuilder {
        private final Map<Denomination, Integer> bills = new HashMap<>();

        public CashBuilder with(Denomination denomination, int count) {
            if (count < 0) {
                throw new IllegalArgumentException("Count must be positive, but " + count);
            } else if (count == 0) {
                return this;
            }
            Integer current = bills.getOrDefault(denomination, 0);
            current += count;
            bills.put(denomination, current);
            return this;
        }

        public CashImpl build() {
            return new CashImpl(bills);
        }
    }

    @Override
    public String toString() {
        var billsString = Denomination.SORTED_BY_VALUE_DESC.stream()
                .filter(x -> bills.getOrDefault(x, 0) != 0)
                .map(x -> x.name() + "=" + bills.get(x))
                .collect(Collectors.joining(", "));
        return String.format("Cash(%s)", billsString);
    }
}

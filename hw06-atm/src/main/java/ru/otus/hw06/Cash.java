package ru.otus.hw06;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cash {
    private final Map<Denomination, Integer> bills;

    private Cash(Map<Denomination, Integer> bills) {
        this.bills = new HashMap<>(bills);
    }

    public Cash add(Cash cash) {
        Cash result = new Cash(this.bills);
        cash.bills.forEach((k, v) -> result.bills.merge(k, v, Integer::sum));
        return result;
    }

    public Cash subtract(Cash cash) {
        Cash result = new Cash(this.bills);
        cash.bills.forEach((k, v) -> {
            Integer count = result.bills.getOrDefault(k, 0);
            if (count < v) {
                throw new CashOperationException("Not enough bills of " + k);
            }
            result.bills.put(k, count - v);
        });
        return result;
    }

    public Map<Denomination, Integer> getBills() {
        return new HashMap<>(bills);

    }

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

        public Cash build() {
            return new Cash(bills);
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

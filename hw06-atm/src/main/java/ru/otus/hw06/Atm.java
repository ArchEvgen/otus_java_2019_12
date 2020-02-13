package ru.otus.hw06;

public class Atm {
    private Cash cash;

    public Atm(Cash initialCash) {
        this.cash = initialCash;
    }

    public void push(Cash cash) {
        this.cash = this.cash.add(cash);
    }

    public Cash pull(int sum) {
        int initialPullSum = sum;
        if (cash.getSum() < sum) {
            throw new AtmOperationException("Not enough founds to pull " + initialPullSum);
        }
        var currentBills = cash.getBills();
        var cashToPull = Cash.builder();
        for (var denomination : Denomination.SORTED_BY_VALUE_DESC) {
            var currentCount = currentBills.getOrDefault(denomination, 0);
            var countToPull = 0;
            while (sum >= denomination.getValue() && currentCount > countToPull) {
                countToPull ++;
                sum -= denomination.getValue();
            }
            cashToPull.with(denomination, countToPull);
        }
        if (sum != 0) {
            throw new AtmOperationException("Can not pull sum " + initialPullSum);
        }
        var result = cashToPull.build();
        this.cash = this.cash.subtract(result);
        return result;
    }

    public int getCashSum() {
        return cash.getSum();
    }

    @Override
    public String toString() {
        return "Atm(" + cash.toString() + ")";
    }
}

package ru.otus.hw06;

public class AtmImpl implements Atm {
    private Cash cash;

    public AtmImpl(Cash initialCash) {
        this.cash = initialCash;
    }

    @Override
    public void push(Cash cash) {
        this.cash = this.cash.add(cash);
    }

    @Override
    public Cash pull(int sum) {
        int initialPullSum = sum;
        if (cash.getSum() < sum) {
            throw new AtmOperationException("Not enough founds to pull " + initialPullSum);
        }
        var cashToPull = CashImpl.builder();
        for (var denomination : Denomination.SORTED_BY_VALUE_DESC) {
            var currentCount = cash.getBills(denomination);
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

    @Override
    public int getCashSum() {
        return cash.getSum();
    }

    @Override
    public String toString() {
        return "Atm(" + cash.toString() + ")";
    }
}

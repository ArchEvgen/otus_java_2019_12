package ru.otus.hw06;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public enum Denomination {
    V1(1), V5(5), V10(10), V50(50), V100(100), V1000(1000);

    private Denomination(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public final static Collection<Denomination> SORTED_BY_VALUE_DESC = Arrays.stream(Denomination.values())
            .sorted(Comparator.comparingInt(x -> -x.value)).collect(Collectors.toUnmodifiableList());

    private final static Map<Integer, Denomination> dictValues = Arrays.stream(Denomination.values())
            .collect(Collectors.toMap(Denomination::getValue, x -> x));

    public static Denomination of(int value) {
        return dictValues.get(value);
    }
}

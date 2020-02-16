package ru.otus.hw06;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CashImplTest {
    private CashImpl cash;

    private static CashImpl createInitialCash() {
        return CashImpl.builder()
                .with(Denomination.V1000, 1)
                .with(Denomination.V100, 2)
                .with(Denomination.V50, 3)
                .with(Denomination.V10, 4)
                .with(Denomination.V5, 5)
                .with(Denomination.V1, 6)
                .build();
    }

    @BeforeEach
    void setUp() {
        cash = createInitialCash();
    }

    @Test
    void testAdd() {
        var toAdd = CashImpl.builder()
                .with(Denomination.V1000, 1)
                .with(Denomination.V100, 0)
                .with(Denomination.V50, 1)
                .with(Denomination.V10, 0)
                .with(Denomination.V5, 1)
                .with(Denomination.V1, 0)
                .build();
        Cash result = cash.add(toAdd);
        Assertions.assertThat(result).isEqualTo(CashImpl.builder()
                .with(Denomination.V1000, 2)
                .with(Denomination.V100, 2)
                .with(Denomination.V50, 4)
                .with(Denomination.V10, 4)
                .with(Denomination.V5, 6)
                .with(Denomination.V1, 6)
                .build());
        Assertions.assertThat(cash).isEqualTo(createInitialCash())
                .withFailMessage("Cash object changed!");
    }

    @Test
    void testSubtract() {
        var diff = CashImpl.builder()
                .with(Denomination.V1000, 1)
                .with(Denomination.V50, 1)
                .with(Denomination.V5, 1)
                .build();
        Cash result = cash.subtract(diff);
        Assertions.assertThat(result).isEqualTo(CashImpl.builder()
                .with(Denomination.V1000, 0)
                .with(Denomination.V100, 2)
                .with(Denomination.V50, 2)
                .with(Denomination.V10, 4)
                .with(Denomination.V5, 4)
                .with(Denomination.V1, 6)
                .build());
        Assertions.assertThat(cash).isEqualTo(createInitialCash())
                .withFailMessage("Cash object changed!");
    }

    @Test
    void testSubtractError() {
        var diff = CashImpl.builder()
                .with(Denomination.V1, 9)
                .build();
        Assertions.assertThatThrownBy(() -> cash.subtract(diff)).isInstanceOf(CashOperationException.class);
        Assertions.assertThat(cash).isEqualTo(createInitialCash())
                .withFailMessage("Cash object changed!");
    }

    @Test
    void testGetSum() {
        Assertions.assertThat(cash.getSum()).isEqualTo(1421);
    }

    @Test
    void testEquals() {
        Assertions.assertThat(cash).isEqualTo(createInitialCash());
    }

    @Test
    void testEqualSumAndDifferentBills() {
        var cash1 = CashImpl.builder()
                .with(Denomination.V50, 2)
                .with(Denomination.V10, 1)
                .build();
        var cash2 = CashImpl.builder()
                .with(Denomination.V100, 1)
                .with(Denomination.V5, 2)
                .build();
        Assertions.assertThat(cash1.getSum()).isEqualTo(cash2.getSum());
        Assertions.assertThat(cash1).isNotEqualTo(cash2);
    }
}
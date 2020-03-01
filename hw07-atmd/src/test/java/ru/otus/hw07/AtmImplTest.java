package ru.otus.hw07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AtmImplTest {
    AtmImpl atm;
    Cash initial;

    @BeforeEach
    void setUp() {
        initial = CashImpl.builder()
            .with(Denomination.V1000, 1)
            .with(Denomination.V100, 2)
            .with(Denomination.V50, 3)
            .with(Denomination.V10, 4)
            .with(Denomination.V5, 5)
            .with(Denomination.V1, 6)
            .build();
        atm = new AtmImpl(initial);
    }

    @Test
    public void testPull() {
        var initialSum = atm.getCashSum();
        var diffSum = 1235;
        var diff = atm.pull(diffSum);
        assertThat(diff.getSum()).isEqualTo(diffSum);
        assertThat(diff).isEqualTo(CashImpl.builder()
                .with(Denomination.V1000, 1)
                .with(Denomination.V100, 2)
                .with(Denomination.V10, 3)
                .with(Denomination.V5, 1)
                .build());
        assertThat(atm.getCashSum()).isEqualTo(initialSum - diffSum);
    }

    @Test
    public void testPullNotEnoughFounds() {
        var initialSum = atm.getCashSum();
        var diffSum = 9999999;
        assertThatThrownBy(() -> atm.pull(diffSum))
                .isInstanceOf(AtmOperationException.class)
                .hasMessage("Not enough founds to pull " + diffSum);
        assertThat(atm.getCashSum()).isEqualTo(initialSum)
                .withFailMessage("Atm cash sum changed!");
    }

    @Test
    public void testPullCanNot() {
        var initialSum = atm.getCashSum();
        var diffSum = 900;
        assertThatThrownBy(() -> atm.pull(diffSum))
                .isInstanceOf(AtmOperationException.class)
                .hasMessage("Can not pull sum " + diffSum);
        assertThat(atm.getCashSum()).isEqualTo(initialSum)
                .withFailMessage("Atm cash sum changed!");
    }
}

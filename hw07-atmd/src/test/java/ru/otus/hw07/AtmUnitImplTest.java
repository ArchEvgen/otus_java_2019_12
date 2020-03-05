package ru.otus.hw07;


import java.util.UUID;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AtmUnitImplTest {
    AtmUnitImpl atm;
    int listenerState;
    int listenerCallsCounter;
    Consumer<AtmUnit> handleAtmOnCashChanged;
    Cash initial;

    @BeforeEach
    void setUp() {
        handleAtmOnCashChanged = serviceAtm -> {
            listenerState = serviceAtm.getCashSum();
            listenerCallsCounter++;
        };
        initial = CashImpl.builder()
                .with(Denomination.V1000, 1)
                .with(Denomination.V100, 2)
                .with(Denomination.V50, 3)
                .with(Denomination.V10, 4)
                .with(Denomination.V5, 5)
                .with(Denomination.V1, 6)
                .build();
        atm = new AtmUnitImpl(UUID.randomUUID(), new AtmImpl(initial), initial);
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


    @Test
    public void testOnCashChanged() {
        atm.addOnCashChangedHandler(handleAtmOnCashChanged);
        var expected = atm.getCashSum() - 101;
        atm.pull(101);
        assertThat(listenerState).isEqualTo(expected);
        assertThat(listenerCallsCounter).isEqualTo(1);

        expected = expected + 30;
        atm.push(CashImpl.builder().with(Denomination.V10, 3).build());
        assertThat(listenerState).isEqualTo(expected);
        assertThat(listenerCallsCounter).isEqualTo(2);

        expected = initial.getSum();
        atm.encash();
        assertThat(listenerState).isEqualTo(expected);
        assertThat(listenerCallsCounter).isEqualTo(3);

        atm.removeOnCashChangedHandler(handleAtmOnCashChanged);
        atm.push(CashImpl.builder().with(Denomination.V10, 3).build());
        assertThat(listenerState).isEqualTo(expected);
        assertThat(listenerCallsCounter).isEqualTo(3);
    }
}

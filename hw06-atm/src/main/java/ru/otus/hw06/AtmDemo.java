package ru.otus.hw06;

public class AtmDemo {
    public static void main(String... args) {
        Atm atm = createAtm();
        System.out.println(String.format("Initial state: %s, total: %s", atm, atm.getCashSum()));

        Cash a = Cash.builder()
                .with(Denomination.V100, 2)
                .with(Denomination.V5, 1)
                .build();
        atm.push(a);
        System.out.println(String.format("After push(%s) state: %s", a, atm));

        Cash b = atm.pull(1234);
        System.out.println(String.format("After pull(%s) state: %s, returns: %s", b.getSum(), atm, b));

        Integer c = 10000;
        try {
            atm.pull(c);
        } catch (AtmOperationException e) {
            System.out.println(String.format("After pull(%s) state: %s, throws: %s", c, atm, e));
        }

        Integer d = 99;
        try {
            atm.pull(d);
        } catch (AtmOperationException e) {
            System.out.println(String.format("After pull(%s) state: %s, throws: %s", d, atm, e));
        }
    }

    private static Atm createAtm() {
        return new Atm(Cash.builder()
                .with(Denomination.V1000, 1)
                .with(Denomination.V100, 0)
                .with(Denomination.V50, 6)
                .with(Denomination.V10, 3)
                .with(Denomination.V5, 1)
                .with(Denomination.V1, 10)
                .build());
    }
}

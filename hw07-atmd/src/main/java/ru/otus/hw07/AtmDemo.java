package ru.otus.hw07;

import java.util.List;
import java.util.function.Consumer;

public class AtmDemo {
    static Consumer<AtmUnit> printAtmStateAction = atm -> {
        System.out.println(String.format("Unit %s sum %s", atm.getId(), atm.getCashSum()));
    };

    public static void main(String... args) {
        Department department = new DepartmentImpl();
        var atmFactory1 = new AtmUnitFactory(CashImpl.builder()
                .with(Denomination.V1000, 10)
                .with(Denomination.V100, 10)
                .with(Denomination.V50, 10)
                .with(Denomination.V10, 100)
                .with(Denomination.V5, 100)
                .with(Denomination.V1, 100)
                .build());
        var atmFactory2 = new AtmUnitFactory(CashImpl.builder().build());
        var atmList = List.of(atmFactory1.createAtm(), atmFactory1.createAtm(), atmFactory2.createAtm(), atmFactory2.createAtm());
        atmList.forEach(department::addAtm);
        department.executeCommand(printAtmStateAction);
        department.executeCommand(x -> x.addOnCashChangedHandler(printAtmStateAction));
        System.out.println("DepartmentCashSum: " + department.getDepartmentCashSum());

        atmList.get(0).pull(1200);
        atmList.get(3).push(CashImpl.builder().with(Denomination.V50, 4).build());
        System.out.println("DepartmentCashSum: " + department.getDepartmentCashSum());

        department.encashAll();
        System.out.println("DepartmentCashSum: " + department.getDepartmentCashSum());
    }
}

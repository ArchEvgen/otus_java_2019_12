package ru.otus.hw07;

import java.util.UUID;
import java.util.function.Consumer;

public interface AtmUnit extends Atm, ServiceAtm {
    /**
     * @return Внутренний идентификатор АТМ
     */
    UUID getId();

    /**
     * Инкассирует АТМ. Возвращает всю наличность в автомате и устанавливает начальное состояние.
     * @return наличность в автомате на момент инкассации
     */
    Cash encash();

    void addOnCashChangedHandler(Consumer<AtmUnit> listener);

    void removeOnCashChangedHandler(Consumer<AtmUnit> listener);
}

package ru.otus.hw07;

import java.util.function.Consumer;

public interface ServiceAtm {
    int getCashSum();

    /**
     * Инкассирует АТМ. Возвращает всю наличность в автомате и устанавливает указанное состояние.
     * @param newInitialSum новое состояние, которое будет установлено после инкассации
     * @return наличность в автомате на момент инкассации
     */
    Cash encash(Cash newInitialSum);
}

# Исследование различных сборщиков мусора 
## Описание
### Стенд
MacBook Pro (15-inch, 2019);
2,6 GHz Intel Core i7;
16 GB 2400 MHz DDR4
### Работа программы 
Класс Benchmark заполняет LinkedList<Instant> с паузой 100мс, замеряя задержку между вставками.  
Через каждые 100000 вставок из списка удаляется 90000 элементов, таким образом список постепенно растет и в итоге программа падает с OutOfMemoryError.
### Способ измерения
В ходе работы программы интересующие нас показатели записываются в поля обьектов Benchmark и GcMonitoring. 
После запуска программы подключался к процессу в VisualVM чтобы следить за потреблением ресурсов. 
После завершения работы программы сохраненный heap dump file открывается с помощью VisualVM, значения полей обьектов Benchmark и GcMonitoring записываются в таблицу.
Для каждого исследуемого сборщика мусора было сделано несколько измерений.

Uptime - время работы цикла Benchmark.

Iterations count - количество вставок в список, показатель количества проделанной работы.

Sum latency - суммарная задержка между вставками.

Max latency - максимальная задержка между вставками.

Young calls - количество young сборок мусора.

Young sum duration - суммарное время young сборок мусора.

Young max duration - максимальное время young сборок мусора.

Old calls | Old sum duration | Old max duration - аналогично для old сборок мусора.

## Результаты измерений
Test | **Uptime** | **Iterations count** | **Sum latency** | **Max latency** | Young calls | Young sum duration | Young max duration | Old calls | Old sum duration | Old max duration 
--- | --- | --- | --- | --- |--- |--- |--- |--- |--- |--- 
G1-1 | 187938 | 109994495 | 60655 | 796 | 285 | 18002 | 243 | 92 | 43758 | 746
G1-2 | 185299 | 109990536 | 60114 | 839 | 281 | 16770 | 214 | 89 | 43587 | 826 
G1-3 | 183204 | 110096056 | 57556 | 800 | 273 | 17378 | 229 | 84 | 40244 | 767 
SerialGC-1 | 252487 | 106793954 | 124813 | 4519 | 39 | 1572 | 138 | 158 | 104326 | 861 
SerialGC-2 | 261816 | 106793157 | 133692 | 2142 | 39 | 1574 | 147 | 158 | 111546 | 1016 
CMS-1 | 270577 | 106792770 | 139019 | 2901 | 52 | 6572 | 284 | 135 | 137518 | 1781
CMS-2 | 254519 | 106792702 | 126633 | 2430 | 47 | 6706 | 272 | 130 | 129589 | 1770
ParallelGC-1 | 181248 | 85790917 | 83317 | 1791 | 65 | 5794 | 145 | 87 | 66013 | 1164
ParallelGC-2 | 181081 | 85791634 | 83476 | 1858 | 65 | 5778 | 128 | 87 | 65534 | 1162

По результатам экспериментов видно, что во всех случаях задержки пропорцинальны времени сборки мусора. 
По графикам VisualVM сильнее всего отличался SerialGC, он быстрее всех сьедал память и запускал old сборки, при этом загрузка ЦП была минимальной.

## Выводы
По количеству проделанной работы в единицу времени безусловным лидером оказался G1, он же лучший по показателям средней и максимальной задержки.

Тесты с ParallelGC хоть и показали минимальный Uptime и время young сборок, но количество итераций в них было существенно меньше чем для других gc,
т.о. производительность была ниже G1, а задержки были существенно выше.

CMS и SerialGC также уступают в этом тесте сборщику G1 по всем показателям.

## UPD. Результаты измерений без Thread.sleep 
Test | **Uptime** | **Iterations count** | **Sum latency** | **Max latency** | Young calls | Young sum duration | Young max duration | Old calls | Old sum duration | Old max duration 
--- | --- | --- | --- | --- |--- |--- |--- |--- |--- |--- 
SerialGC | 158307 | 106793130 | 144391 | 6143 | 37 | 1463 | 129 | 156 | 122739 | 1583
ParallelGC | 95607 | 85700000 | 89396 | 2246 | 63 | 5307 | 127 | 85 | 69081 | 980
CMS | 160746 | 106792451 | 146518 | 2246 | 39 | 3944 | 289 | 109 | 111791 | 1900
G1 | 77512 | 109900000 | 65783 | 778 | 292 | 15389 | 231 | 101 | 49780 | 787

Преимущество G1 стало еще очевиднее.  
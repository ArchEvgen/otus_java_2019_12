package ru.otus.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DiyArrayListDemo {
    public static void main(String... args) {
        var arrayList = new ArrayList<>(List.of(2.0, 5.1, -1.32, 0.0, 123.4, 0.32, 0.0, -32.0));
        var myList = new DiyArrayList<>(arrayList);
        System.out.println("Created initial myList: " + myList.toString());

        var toAdd = new Double[] {10.0, 20.0, 30.0, 40.0};
        System.out.println("Trying to addAll of " + Arrays.toString(toAdd));
        Collections.addAll(arrayList, toAdd);
        Collections.addAll(myList, toAdd);
        System.out.println("MyList: " + myList.toString());
        checkElementsEquals(myList, arrayList);

        var myList2 = new DiyArrayList<Double>(Collections.nCopies(myList.size(), 0.0));
        System.out.println("Trying to copy myList to myList2");
        Collections.copy(myList2, myList);
        System.out.println("MyList2: " + myList2.toString());
        checkElementsEquals(myList2, arrayList);

        System.out.println("Trying to sort myList");
        Collections.sort(myList, Double::compareTo);
        Collections.sort(arrayList, Double::compareTo);
        System.out.println("MyList: " + myList.toString());
        checkElementsEquals(myList, arrayList);
    }

    private static <T> void checkElementsEquals(List<T> list, List<T> expected) {
        boolean areEquals = true;
        Iterator targetIt = list.iterator();
        for (T obj:expected)
            if (!obj.equals(targetIt.next())) {
                areEquals = false;
                break;
            }
        System.out.println(areEquals ? "Success" : "Fail");
        System.out.println();
    }
}

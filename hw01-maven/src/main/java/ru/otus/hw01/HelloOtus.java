package ru.otus.hw01;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class HelloOtus {
    public static void main(String... args) {
        if (args.length == 0) {
            args = new String[]{"hello", "otus!", "java", "2019-12"};
        }
        var resultList = Lists.transform(Lists.newArrayList(args), HelloOtus::capitalize);
        String result = Joiner.on(" ").join(resultList);
        System.out.println(result);
    }

    private static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}

package ru.otus.hw08;

import java.time.LocalDate;
import java.util.Map;

import lombok.Data;

@Data
public class Person {
    private LocalDate birthDay;
    private String name;
    private Map<String, String> about;
}

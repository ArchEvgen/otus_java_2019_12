package ru.otus.hw08;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends Person {
    private int id;
    private Set<Grants> grants;
    private int[] ints;
}

package ru.otus.hw09.core.model;

import java.math.BigDecimal;

import lombok.Data;
import ru.otus.hw09.orm.Id;

@Data
public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;
    private boolean admin;
}

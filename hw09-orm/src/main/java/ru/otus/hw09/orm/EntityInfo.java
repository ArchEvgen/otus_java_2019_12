package ru.otus.hw09.orm;

import java.lang.reflect.Field;
import java.util.List;

import lombok.Data;

@Data
public class EntityInfo {
    private final Class entityClass;
    private final String tableName;
    private final Field keyField;
    private final List<Field> fields;
}

package ru.otus.hw09.orm;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class SqlQueryGenerator implements QueryGenerator {
    @Override
    public String insert(EntityInfo entityInfo) {
        String fields = entityInfo.getFields().stream().map(Field::getName).collect(Collectors.joining(", "));
        String values = entityInfo.getFields().stream().map(x -> "?").collect(Collectors.joining(", "));
        return "insert into " + entityInfo.getTableName() +
                "(" + fields + ")" +
                " values (" + values + ")";
    }

    @Override
    public String update(EntityInfo entityInfo) {
        String fields = entityInfo.getFields().stream().map(x -> x.getName() + " = ?")
                .collect(Collectors.joining(", "));
        return "update " + entityInfo.getTableName() +
                " set " + fields +
                " where " + entityInfo.getKeyField().getName() + " = ?";
    }

    @Override
    public String select(EntityInfo entityInfo) {
        return "select " + entityInfo.getFields().stream().map(Field::getName).collect(Collectors.joining(", ")) +
                " from " + entityInfo.getTableName() +
                " where " + entityInfo.getKeyField().getName() + " = ?";
    }
}

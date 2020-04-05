package ru.otus.hw09.orm;

public interface QueryGenerator {
    String insert(EntityInfo entityInfo);
    String update(EntityInfo entityInfo);
    String select(EntityInfo entityInfo);
}

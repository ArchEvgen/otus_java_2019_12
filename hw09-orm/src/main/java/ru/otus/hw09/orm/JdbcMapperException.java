package ru.otus.hw09.orm;

public class JdbcMapperException extends RuntimeException {
    public JdbcMapperException(String message) {
        super(message);
    }

    public JdbcMapperException(String message, Throwable ex) {
        super(message, ex);
    }
}

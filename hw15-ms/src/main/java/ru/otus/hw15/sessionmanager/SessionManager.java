package ru.otus.hw15.sessionmanager;

public interface SessionManager extends AutoCloseable {
  void beginSession();

  void commitSession();

  void rollbackSession();

  void close();

  DatabaseSession getCurrentSession();
}

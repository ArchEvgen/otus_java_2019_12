package ru.otus.hw09.core.dao;

public class AccountDaoException extends RuntimeException {
  public AccountDaoException(Exception ex) {
    super(ex);
  }
}

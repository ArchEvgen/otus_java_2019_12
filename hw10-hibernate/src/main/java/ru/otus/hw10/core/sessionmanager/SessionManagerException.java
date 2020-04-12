package ru.otus.hw10.core.sessionmanager;


public class SessionManagerException extends RuntimeException {
  public SessionManagerException(String msg) {
    super(msg);
  }

  public SessionManagerException(Exception ex) {
    super(ex);
  }
}

package ru.otus.hw09.core.dao;

import java.util.Optional;

import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;

public interface AccountDao {
  Optional<Account> findById(long id);

  long saveAccount(Account user);

  void updateAccount(Account user);

  SessionManager getSessionManager();
}

package ru.otus.hw09.core.service;

import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;

import java.util.Optional;

public interface DBServiceUser {
  long saveUser(User user);

  Optional<User> getUser(long id);

  void updateUser(User user);

  long saveAccount(Account account);

  Optional<Account> getAccount(long id);
}

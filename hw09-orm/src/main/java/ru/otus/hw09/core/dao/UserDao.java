package ru.otus.hw09.core.dao;

import java.util.Optional;

import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;

public interface UserDao {
  Optional<User> findById(long id);

  long saveUser(User user);

  void updateUser(User user);

  SessionManager getSessionManager();
}

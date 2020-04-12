package ru.otus.hw10.core.dao;

import java.util.Optional;

import ru.otus.hw10.core.model.User;
import ru.otus.hw10.core.sessionmanager.SessionManager;

public interface UserDao {
  Optional<User> findById(long id);

  long saveUser(User user);

  SessionManager getSessionManager();

  void updateUser(User user);
}

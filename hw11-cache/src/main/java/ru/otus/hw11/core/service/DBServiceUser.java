package ru.otus.hw11.core.service;

import ru.otus.hw11.core.model.User;

import java.util.Optional;

public interface DBServiceUser {
  long saveUser(User user);

  Optional<User> getUser(long id);

  void updateUser(User user);
}

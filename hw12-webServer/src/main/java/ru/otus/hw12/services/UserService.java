package ru.otus.hw12.services;

import java.util.List;
import java.util.Optional;

import ru.otus.hw12.model.User;

public interface UserService {
    Optional<User> findById(long id);
    Optional<User> findByLogin(String login);
    List<User> getAll();
    long saveUser(User user);
}

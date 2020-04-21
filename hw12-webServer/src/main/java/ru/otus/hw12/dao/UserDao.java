package ru.otus.hw12.dao;

import ru.otus.hw12.model.User;
import ru.otus.hw12.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    SessionManager getSessionManager();

    Optional<User> findById(long id);
    Optional<User> findByLogin(String login);
    List<User> getAll();
    long saveUser(User user);
}
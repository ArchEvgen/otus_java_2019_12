package ru.otus.hw13.dao;

import ru.otus.hw13.model.User;
import ru.otus.hw13.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    SessionManager getSessionManager();

    Optional<User> findById(long id);
    Optional<User> findByLogin(String login);
    List<User> getAll();
    long saveUser(User user);
}
package ru.otus.hw13.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import ru.otus.hw13.dao.UserDao;
import ru.otus.hw13.model.User;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(long id) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            return userDao.findById(id);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            return userDao.findByLogin(login);
        }
    }

    @Override
    public List<User> getAll() {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            return userDao.getAll();
        }
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            return userDao.saveUser(user);
        }
    }
}

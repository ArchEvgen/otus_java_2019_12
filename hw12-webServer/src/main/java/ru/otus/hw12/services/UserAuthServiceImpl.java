package ru.otus.hw12.services;

import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.sessionmanager.SessionManager;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.findByLogin(login)
                        .map(user -> user.getPassword().equals(password))
                        .orElse(false);

            } catch (Exception e) {
                sessionManager.rollbackSession();
            }
        }
        return false;
    }
}

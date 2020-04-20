package ru.otus.hw12.services;

import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.model.User;
import ru.otus.hw12.sessionmanager.SessionManager;

public class UserTestDataService {
    private final UserDao userDao;

    public UserTestDataService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void fillDb() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            userDao.saveUser(new User(0L, "Крис Гир", "user1", "11111"));
            userDao.saveUser(new User(0L, "Ая Кэш", "user2", "11111"));
            userDao.saveUser(new User(0L, "Десмин Боргес", "user3", "11111"));
            userDao.saveUser(new User(0L, "Кетер Донохью", "user4", "11111"));
            userDao.saveUser(new User(0L, "Стивен Шнайдер", "user5", "11111"));
            userDao.saveUser(new User(0L, "Джанет Вэрни", "user6", "11111"));
            userDao.saveUser(new User(0L, "Брэндон Смит", "user7", "11111"));
        }
    }
}

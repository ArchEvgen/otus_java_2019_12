package ru.otus.hw12.services;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Password;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.model.User;
import ru.otus.hw12.sessionmanager.SessionManager;

import java.util.Optional;

public class UserDaoLoginServiceImpl extends AbstractLoginService {

    private final UserDao userDao;

    public UserDaoLoginServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    protected String[] loadRoleInfo(UserPrincipal userPrincipal) {
        return new String[] {"user"};
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                System.out.println(String.format("InMemoryLoginService#loadUserInfo(%s)", login));
                Optional<User> dbUser = userDao.findByLogin(login);
                return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);

            } catch (Exception e) {
                sessionManager.rollbackSession();
            }
        }
        return null;
    }
}

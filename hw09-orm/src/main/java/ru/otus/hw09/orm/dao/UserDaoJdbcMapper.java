package ru.otus.hw09.orm.dao;

import java.sql.Connection;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.dao.UserDaoException;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.orm.JdbcMapper;

@RequiredArgsConstructor
@Slf4j
public class UserDaoJdbcMapper implements UserDao {
    private final JdbcMapper<User> jdbcMapper;
    private final SessionManagerJdbc sessionManager;

    @Override
    public Optional<User> findById(long id) {
        try {
            return Optional.ofNullable(jdbcMapper.getById(getConnection(), id));
        } catch (Exception ex) {
            throw new UserDaoException(ex);
        }
    }

    @Override
    public long saveUser(User user) {
        try {
            return jdbcMapper.insert(getConnection(), user);
        } catch (Exception ex) {
            throw new UserDaoException(ex);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            jdbcMapper.update(getConnection(), user);
        } catch (Exception ex) {
            throw new UserDaoException(ex);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}

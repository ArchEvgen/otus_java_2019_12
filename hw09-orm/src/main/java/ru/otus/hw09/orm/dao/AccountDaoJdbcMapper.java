package ru.otus.hw09.orm.dao;

import java.sql.Connection;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw09.core.dao.AccountDao;
import ru.otus.hw09.core.dao.AccountDaoException;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.orm.JdbcMapper;

@RequiredArgsConstructor
@Slf4j
public class AccountDaoJdbcMapper implements AccountDao {
    private final JdbcMapper<Account> jdbcMapper;
    private final SessionManagerJdbc sessionManager;

    @Override
    public Optional<Account> findById(long id) {
        try {
            return Optional.ofNullable(jdbcMapper.getById(getConnection(), id));
        } catch (Exception ex) {
            throw new AccountDaoException(ex);
        }
    }

    @Override
    public long saveAccount(Account user) {
        try {
            return jdbcMapper.insert(getConnection(), user);
        } catch (Exception ex) {
            throw new AccountDaoException(ex);
        }
    }

    @Override
    public void updateAccount(Account user) {
        try {
            jdbcMapper.update(getConnection(), user);
        } catch (Exception ex) {
            throw new AccountDaoException(ex);
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

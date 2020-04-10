package ru.otus.hw09.jdbc.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.core.dao.AccountDao;
import ru.otus.hw09.core.dao.AccountDaoException;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.DbExecutor;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.orm.JdbcMapper;

public class AccountDaoJdbc implements AccountDao {
    private static Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<Account> dbExecutor;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<Account> dbExecutor, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        try {
            return dbExecutor.selectRecord(getConnection(), jdbcMapper.getSelectByIdQuery(), id, jdbcMapper::readObject);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AccountDaoException(e);
        }
        return Optional.empty();
    }

    @Override
    public long saveAccount(Account Account) {
        try {
            return dbExecutor.insertRecord(getConnection(), jdbcMapper.getInsertQuery(), pst ->
                    jdbcMapper.addInsertQueryParams(pst, Account));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AccountDaoException(e);
        }
    }

    @Override
    public void updateAccount(Account Account) {
        try {
            dbExecutor.updateRecord(getConnection(), jdbcMapper.getUpdateQuery(), pst ->
                    jdbcMapper.addUpdateQueryParams(pst, Account));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AccountDaoException(e);
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

package ru.otus.hw09.jdbc.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.dao.UserDaoException;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.DbExecutor;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.orm.JdbcMapper;

public class UserDaoJdbc implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<User> dbExecutor;
  private final JdbcMapper<User> jdbcMapper;

  public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor, JdbcMapper<User> jdbcMapper) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.jdbcMapper = jdbcMapper;
  }

  @Override
  public Optional<User> findById(long id) {
    try {
      return dbExecutor.selectRecord(getConnection(), jdbcMapper.getSelectByIdQuery(), id, jdbcMapper::readObject);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
    return Optional.empty();
  }

  @Override
  public long saveUser(User user) {
    try {
      return dbExecutor.insertRecord(getConnection(), jdbcMapper.getInsertQuery(), pst ->
              jdbcMapper.addInsertQueryParams(pst, user));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public void updateUser(User user) {
    try {
      dbExecutor.updateRecord(getConnection(), jdbcMapper.getUpdateQuery(), pst ->
              jdbcMapper.addUpdateQueryParams(pst, user));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
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

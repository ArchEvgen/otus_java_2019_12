package ru.otus.hw09.jdbc.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.dao.UserDaoException;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.DbExecutor;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;

public class UserDaoJdbc implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<User> dbExecutor;

  public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
  }


  @Override
  public Optional<User> findById(long id) {
    try {
      return dbExecutor.selectRecord(getConnection(), "select id, name from user where id  = ?", id, resultSet -> {
        try {
          if (resultSet.next()) {
            return new User(resultSet.getLong("id"), resultSet.getString("name"));
          }
        } catch (SQLException e) {
          logger.error(e.getMessage(), e);
        }
        return null;
      });
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveUser(User user) {
    try {
      return dbExecutor.insertRecord(getConnection(), "insert into user(name) values (?)", Collections.singletonList(user.getName()));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public void updateUser(User user) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }

  private Connection getConnection() {
    return sessionManager.getCurrentSession().getConnection();
  }
}

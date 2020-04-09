package ru.otus.hw09.core.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.core.dao.AccountDao;
import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.sessionmanager.SessionManager;

public class DbServiceUserImpl implements DBServiceUser {
  private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

  private final UserDao userDao;
  private final AccountDao accountDao;

  public DbServiceUserImpl(UserDao userDao, AccountDao accountDao) {
    this.userDao = userDao;
    this.accountDao = accountDao;
  }

  @Override
  public long saveUser(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long userId = userDao.saveUser(user);
        sessionManager.commitSession();

        logger.info("created user: {}", userId);
        return userId;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }


  @Override
  public Optional<User> getUser(long id) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> userOptional = userDao.findById(id);

        logger.info("user: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public void updateUser(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        userDao.updateUser(user);
        sessionManager.commitSession();

        logger.info("updated user: {}", user.getId());
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public long saveAccount(Account account) {
    try (SessionManager sessionManager = accountDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long id = accountDao.saveAccount(account);
        sessionManager.commitSession();

        logger.info("created account: {}", id);
        return id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public void updateAccount(Account account) {
    try (SessionManager sessionManager = accountDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        accountDao.updateAccount(account);
        sessionManager.commitSession();

        logger.info("updated Account: {}", account.getNo());
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional<Account> getAccount(long id) {
    try (SessionManager sessionManager = accountDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<Account> userOptional = accountDao.findById(id);

        logger.info("account: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

}

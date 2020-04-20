package ru.otus.hw12.hibernate.dao;


import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.dao.UserDaoException;
import ru.otus.hw12.model.User;
import ru.otus.hw12.sessionmanager.SessionManager;
import ru.otus.hw12.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hw12.hibernate.sessionmanager.SessionManagerHibernate;

public class UserDaoHibernate implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);
  private final SessionManagerHibernate sessionManager;
  private final Random rnd = new Random();

  public UserDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<User> findById(long id) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> findByLogin(String login) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession()
              .createQuery("select u from User u where u.login = :login", User.class)
              .setParameter("login", login)
              .getSingleResult());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public List<User> getAll() {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return currentSession.getHibernateSession()
              .createQuery("select u from User u", User.class)
              .getResultList();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return List.of();
  }


  @Override
  public long saveUser(User user) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      if (user.getId() > 0) {
        hibernateSession.merge(user);
      } else {
        hibernateSession.persist(user);
      }
      return user.getId();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  public SessionManager getSessionManager() {
    return sessionManager;
  }

}

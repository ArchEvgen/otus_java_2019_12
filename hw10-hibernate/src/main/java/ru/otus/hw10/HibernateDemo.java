package ru.otus.hw10;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.core.dao.UserDao;
import ru.otus.hw10.core.model.Address;
import ru.otus.hw10.core.model.Phone;
import ru.otus.hw10.core.model.User;
import ru.otus.hw10.core.service.DBServiceUser;
import ru.otus.hw10.core.service.DbServiceUserImpl;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.dao.UserDaoHibernate;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

public class HibernateDemo {
  private static Logger logger = LoggerFactory.getLogger(HibernateDemo.class);

  public static void main(String[] args) {
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
            User.class, Address.class, Phone.class);

    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDao userDao = new UserDaoHibernate(sessionManager);
    DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

    var address1 = new Address();
    address1.setStreet("road66");
    var phone1 = new Phone();
    phone1.setNumber("03");
    var user1 = new User();
    user1.setName("Вася");
    user1.setAge(42);
    user1.setAddress(address1);
    user1.setPhones(List.of(phone1));
    phone1.setUser(user1);
    long id = dbServiceUser.saveUser(user1);
    Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);
    printSavedUser(id, mayBeCreatedUser);

    var user2 = new User();
    user2.setId(id);
    user2.setName("Петя");
    user2.setPhones(new ArrayList<>());
    id = dbServiceUser.saveUser(user2);
    Optional<User> mayBeUpdatedUser = dbServiceUser.getUser(id);
    printSavedUser(id, mayBeUpdatedUser);
  }

  private static void printSavedUser(Long id, Optional<User> mayBeUser) {
    System.out.println("-----------------------------------------------------------");
    mayBeUser.ifPresentOrElse(System.out::println, () -> logger.info("User not found"));
  }
}

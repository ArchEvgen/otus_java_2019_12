package ru.otus.hw11;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.otus.hw11.cachehw.MyCache;
import ru.otus.hw11.core.dao.UserDao;
import ru.otus.hw11.core.model.Address;
import ru.otus.hw11.core.model.Phone;
import ru.otus.hw11.core.model.User;
import ru.otus.hw11.core.service.CacheDBServiceUser;
import ru.otus.hw11.core.service.DBServiceUser;
import ru.otus.hw11.core.service.DbServiceUserImpl;
import ru.otus.hw11.hibernate.HibernateUtils;
import ru.otus.hw11.hibernate.dao.UserDaoHibernate;
import ru.otus.hw11.hibernate.sessionmanager.SessionManagerHibernate;

@Slf4j
@RequiredArgsConstructor
public class CacheOverDbDemo {
  private static final long iterations = 10000;
  private static final int readsPerIteration = 10;

  private final DBServiceUser dbServiceUser;
  private final PrimitiveIterator.OfInt rnd = new Random().ints(0, readsPerIteration).iterator();
  private Instant startAt;

  public static void main(String[] args) {
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
            User.class, Address.class, Phone.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDao userDao = new UserDaoHibernate(sessionManager);
    DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
    DBServiceUser cacheDbServiceUser = new CacheDBServiceUser(dbServiceUser, new MyCache<>());
    new CacheOverDbDemo(cacheDbServiceUser).startLoop();
  }

  public void startLoop() {
    startAt = Instant.now();
    for (int i = 0; i < readsPerIteration; i++) {
      saveSomeUser();
    }
    for (int i = readsPerIteration; i < iterations; i++) {
      saveSomeUser();
      for (int j = 0; j < readsPerIteration; j++) {
        printRandomUser(i);
      }
    }
    double duration = Duration.between(startAt, Instant.now()).toNanos() / 1000000000.0;
    log.info("Done demo loop for {}: \nStart: {}; Duration: {}\nIterations: {}; Reads per iteration: {}",
            dbServiceUser.getClass().getSimpleName(),
            startAt, duration, iterations, readsPerIteration);
  }

  private void printRandomUser(int i) {
    var id = i - rnd.next();
    Optional<User> user = dbServiceUser.getUser(id);
    printUser(id, user);
  }

  private void saveSomeUser() {
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
    dbServiceUser.saveUser(user1);
  }

  private void printUser(long id, Optional<User> mayBeUser) {
    log.info("-----------------------------------------------------------");
    mayBeUser.ifPresentOrElse(System.out::println, () -> log.info("User {} not found", id));
  }
}

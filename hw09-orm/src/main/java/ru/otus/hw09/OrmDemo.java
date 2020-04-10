package ru.otus.hw09;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw09.core.dao.AccountDao;
import ru.otus.hw09.core.dao.UserDao;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;
import ru.otus.hw09.core.service.DBServiceUser;
import ru.otus.hw09.core.service.DbServiceUserImpl;
import ru.otus.hw09.h2.DataSourceH2;
import ru.otus.hw09.jdbc.DbExecutor;
import ru.otus.hw09.jdbc.dao.AccountDaoJdbc;
import ru.otus.hw09.jdbc.dao.UserDaoJdbc;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.orm.EntityInfoStorage;
import ru.otus.hw09.orm.JdbcMapper;
import ru.otus.hw09.orm.SqlQueryGenerator;

@Slf4j
public class OrmDemo {
    DataSource dataSource;
    SessionManagerJdbc sessionManager;
    EntityInfoStorage entityInfoStorage;
    JdbcMapper<User> userJdbcMapper;
    JdbcMapper<Account> accountJdbcMapper;
    UserDao userDao;
    AccountDao accountDao;
    DBServiceUser dbServiceUser;

    public static void main(String[] args) throws Exception {
        OrmDemo demo = new OrmDemo();
        demo.init();
        demo.runUserDemo();
        demo.runAccountDemo();
    }

    private void runAccountDemo() {
        var account = new Account();
        account.setType("basic");
        account.setRest(BigDecimal.valueOf(42.22));
        long id = dbServiceUser.saveAccount(account);
        account = dbServiceUser.getAccount(id).get();
        log.info("created account: {}", account.toString());
        account.setType("ultra");
        account.setRest(BigDecimal.valueOf(100500));
        account.setAdmin(true);
        dbServiceUser.updateAccount(account);
        account = dbServiceUser.getAccount(id).get();
        log.info("updated account: {}", account.toString());
    }

    private void runUserDemo() {
        var newUser = new User();
        newUser.setName("Женя");
        newUser.setAge(29);
        long id = dbServiceUser.saveUser(newUser);
        User user = dbServiceUser.getUser(id).get();
        log.info("created user: {}", user.toString());
        user.setName("Евгений");
        user.setAge(30);
        dbServiceUser.updateUser(user);
        user = dbServiceUser.getUser(id).get();
        log.info("updated user: {}", user.toString());
    }

    private void init() throws SQLException {
        dataSource = new DataSourceH2();
        createTable(dataSource);
        sessionManager = new SessionManagerJdbc(dataSource);
        entityInfoStorage = new EntityInfoStorage();
        userJdbcMapper = new JdbcMapper<User>(entityInfoStorage.getEntityInfo(User.class), new SqlQueryGenerator());
        accountJdbcMapper = new JdbcMapper<Account>(entityInfoStorage.getEntityInfo(Account.class), new SqlQueryGenerator());
        userDao = new UserDaoJdbc(sessionManager, new DbExecutor<>(), userJdbcMapper);
        accountDao = new AccountDaoJdbc(sessionManager, new DbExecutor<>(), accountJdbcMapper);
        dbServiceUser = new DbServiceUserImpl(userDao, accountDao);
    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(
                    "create table user(id long auto_increment, name varchar(50), age int(3))")) {
                pst.executeUpdate();
            }
            try (PreparedStatement pst = connection.prepareStatement(
                    "create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number, admin boolean)")) {
                pst.executeUpdate();
            }
        }
        System.out.println("table created");
    }
}

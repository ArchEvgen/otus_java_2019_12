package ru.otus.hw12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
import ru.otus.hw12.dao.InMemoryUserDao;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.hibernate.HibernateUtils;
import ru.otus.hw12.hibernate.dao.UserDaoHibernate;
import ru.otus.hw12.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.hw12.model.User;
import ru.otus.hw12.server.UsersWebServer;
import ru.otus.hw12.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.services.TemplateProcessorImpl;
import ru.otus.hw12.services.UserAuthService;
import ru.otus.hw12.services.UserAuthServiceImpl;
import ru.otus.hw12.sessionmanager.SessionManager;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // Админка
    http://localhost:8080/admin

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        fillDb(userDao);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void fillDb(UserDao userDao) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            userDao.saveUser(new User(0L, "Крис Гир", "user1", "11111"));
            userDao.saveUser(new User(0L, "Ая Кэш", "user2", "11111"));
            userDao.saveUser(new User(0L, "Десмин Боргес", "user3", "11111"));
            userDao.saveUser(new User(0L, "Кетер Донохью", "user4", "11111"));
            userDao.saveUser(new User(0L, "Стивен Шнайдер", "user5", "11111"));
            userDao.saveUser(new User(0L, "Джанет Вэрни", "user6", "11111"));
            userDao.saveUser(new User(0L, "Брэндон Смит", "user7", "11111"));
        }
    }
}

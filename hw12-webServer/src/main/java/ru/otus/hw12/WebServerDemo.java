package ru.otus.hw12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
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
import ru.otus.hw12.services.UserService;
import ru.otus.hw12.services.UserServiceImpl;
import ru.otus.hw12.services.UserTestDataService;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Админка
    http://localhost:8080/admin
*/
public class WebServerDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        UserService userService = new UserServiceImpl(userDao);
        var userTestDataService = new UserTestDataService(userDao);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userService, gson, templateProcessor);

        userTestDataService.fillDb();
        usersWebServer.start();
        usersWebServer.join();
    }
}

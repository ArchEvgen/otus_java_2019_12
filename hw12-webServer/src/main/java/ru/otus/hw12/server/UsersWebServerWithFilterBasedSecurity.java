package ru.otus.hw12.server;

import java.util.Arrays;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.services.UserAuthService;
import ru.otus.hw12.services.UserService;
import ru.otus.hw12.servlet.AuthorizationFilter;
import ru.otus.hw12.servlet.LoginServlet;

public class UsersWebServerWithFilterBasedSecurity extends UsersWebServerSimple {
    private final UserAuthService authService;

    public UsersWebServerWithFilterBasedSecurity(int port,
                                                 UserAuthService authService,
                                                 UserService userService,
                                                 Gson gson,
                                                 TemplateProcessor templateProcessor) {
        super(port, userService, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}

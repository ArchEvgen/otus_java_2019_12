package ru.otus.hw12.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.otus.hw12.model.User;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.services.UserService;

public class AdminServlet extends HttpServlet {
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";

    private final TemplateProcessor templateProcessor;
    private final UserService userService;

    public AdminServlet(TemplateProcessor templateProcessor, UserService userService) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        renderAdminPage(response);
    }

    private void renderAdminPage(HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        response.setContentType("text/html");
        paramsMap.put("users", userService.getAll());
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        userService.saveUser(new User(0, name, login, password));
        renderAdminPage(response);
    }

}

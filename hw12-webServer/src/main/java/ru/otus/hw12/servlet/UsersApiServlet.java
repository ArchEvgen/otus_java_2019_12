package ru.otus.hw12.servlet;

import com.google.gson.Gson;
import ru.otus.hw12.model.User;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.sessionmanager.SessionManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final UserDao userDao;
    private final Gson gson;

    public UsersApiServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            User user = userDao.findById(extractIdFromRequest(request)).orElse(null);
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(user));
        }
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}

package ru.otus.hw12;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.model.User;
import ru.otus.hw12.server.UsersWebServer;
import ru.otus.hw12.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.services.TemplateProcessorImpl;
import ru.otus.hw12.services.UserAuthService;
import ru.otus.hw12.services.UserService;
import ru.otus.hw12.services.UserServiceImpl;
import ru.otus.hw12.sessionmanager.SessionManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.otus.hw12.utils.HttpUrlConnectionHelper.buildUrl;
import static ru.otus.hw12.utils.HttpUrlConnectionHelper.readResponseFromConnection;
import static ru.otus.hw12.utils.HttpUrlConnectionHelper.sendRequest;
import static ru.otus.hw12.utils.WebServerHelper.COOKIE_HEADER;
import static ru.otus.hw12.utils.WebServerHelper.login;

@DisplayName("Тест сервера должен ")
class UsersWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String LOGIN_URL = "login";
    private static final String ADMIN_URL = "admin";

    private static final long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_LOGIN = "user1";
    private static final String DEFAULT_USER_PASSWORD = "11111";
    private static final User DEFAULT_USER = new User(DEFAULT_USER_ID, "Вася", DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
    private static final String INCORRECT_USER_LOGIN = "BadUser";
    private static final String TEMPLATES_DIR = "/templates/";

    private static Gson gson;
    private static UsersWebServer webServer;

    @BeforeAll
    static void setUp() throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserDao userDao = mock(UserDao.class);
        when(userDao.getSessionManager()).thenReturn(mock(SessionManager.class));
        when(userDao.getAll()).thenReturn(List.of(DEFAULT_USER));
        UserService userService = new UserServiceImpl(userDao);
        UserAuthService userAuthService = mock(UserAuthService.class);

        given(userAuthService.authenticate(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(true);
        given(userAuthService.authenticate(INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(false);
        given(userDao.findById(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_USER));

        gson = new GsonBuilder().serializeNulls().create();
        webServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, userAuthService, userService, gson, templateProcessor);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать 302 при запросе пользователя по id если не выполнен вход ")
    @Test
    void shouldReturnForbiddenStatusForUserRequestWhenUnauthorized() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, ADMIN_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
    }

    @DisplayName("возвращать ID сессии при выполнении входа с верными данными")
    @Test
    void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();
    }

    @DisplayName("не возвращать ID сессии при выполнении входа если данные входа не верны")
    @Test
    void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNull();
    }

    @DisplayName("возвращать корректные данные при запросе пользователя по id если вход выполнен")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws IOException {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);

        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, ADMIN_URL, null), HttpMethod.GET);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        int responseCode = connection.getResponseCode();
        String response = readResponseFromConnection(connection);

        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response).contains(DEFAULT_USER.getName());
    }
}
package ru.otus.hw12.dao;

import ru.otus.hw12.model.User;
import ru.otus.hw12.sessionmanager.DatabaseSession;
import ru.otus.hw12.sessionmanager.SessionManager;

import java.util.*;

import javax.transaction.NotSupportedException;

public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Крис Гир", "user1", "11111"));
        users.put(2L, new User(2L, "Ая Кэш", "user2", "11111"));
        users.put(3L, new User(3L, "Десмин Боргес", "user3", "11111"));
        users.put(4L, new User(4L, "Кетер Донохью", "user4", "11111"));
        users.put(5L, new User(5L, "Стивен Шнайдер", "user5", "11111"));
        users.put(6L, new User(6L, "Джанет Вэрни", "user6", "11111"));
        users.put(7L, new User(7L, "Брэндон Смит", "user7", "11111"));
    }

    @Override
    public SessionManager getSessionManager() {
        return null;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findRandomUser() {
        Random r = new Random();
        return users.values().stream().skip(r.nextInt(users.size() - 1)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public long saveUser(User user) {
        long id = users.size() + 1;
        users.put(id, user);
        return id;
    }

    private class InMemorySessionManager implements SessionManager{

        @Override
        public void beginSession() {

        }

        @Override
        public void commitSession() {

        }

        @Override
        public void rollbackSession() {
            throw new UserDaoException(new NotSupportedException());
        }

        @Override
        public void close() {

        }

        @Override
        public DatabaseSession getCurrentSession() {
            return null;
        }
    }
}

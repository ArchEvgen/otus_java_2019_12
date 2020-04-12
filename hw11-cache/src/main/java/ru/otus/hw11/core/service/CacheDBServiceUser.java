package ru.otus.hw11.core.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import ru.otus.hw11.cachehw.HwCache;
import ru.otus.hw11.core.model.User;

@RequiredArgsConstructor
public class CacheDBServiceUser implements DBServiceUser {
    private final DBServiceUser originalService;
    private final HwCache<Long, Optional<User>> cache;

    @Override
    public long saveUser(User user) {
        var id = originalService.saveUser(user);
        user.setId(id);
        cache.put(id, Optional.of(user));
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        var user = cache.get(id);
        if (user == null) {
            user = originalService.getUser(id);
            cache.put(id, user);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        originalService.updateUser(user);
        cache.put(user.getId(), Optional.of(user));
    }
}

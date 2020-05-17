package ru.otus.hw16.messagesystem.front;


import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import ru.otus.hw16.model.User;

public interface FrontendService {
    void createUser(User user, Consumer<User> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}


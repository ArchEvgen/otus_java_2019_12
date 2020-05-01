package ru.otus.hw15.messagesystem.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.RequestHandler;
import ru.otus.hw15.messagesystem.Serializer;
import ru.otus.hw15.model.User;
import ru.otus.hw15.services.UserService;

@Service
public class CreateUserRequestHandler implements RequestHandler {
    private final UserService userService;
    private final Serializer serializer;

    public CreateUserRequestHandler(UserService userService, Serializer serializer) {
        this.userService = userService;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User user = serializer.deserialize(msg.getPayload(), User.class);
        userService.saveUser(user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.CREATE_USER.getValue(), serializer.serialize(user)));
    }
}

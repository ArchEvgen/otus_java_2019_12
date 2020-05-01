package ru.otus.hw15.messagesystem;


import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}

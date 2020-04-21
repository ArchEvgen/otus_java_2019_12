package ru.otus.hw12.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}

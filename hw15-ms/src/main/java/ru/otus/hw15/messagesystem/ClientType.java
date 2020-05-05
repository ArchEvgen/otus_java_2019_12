package ru.otus.hw15.messagesystem;

public enum ClientType {
    FRONTEND_SERVICE_CLIENT("frontendService"),
    DATABASE_SERVICE_CLIENT("databaseService");

    private final String value;

    ClientType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

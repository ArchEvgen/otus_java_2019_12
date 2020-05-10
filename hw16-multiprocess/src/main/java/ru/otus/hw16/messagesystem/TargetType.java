package ru.otus.hw16.messagesystem;

public enum TargetType {
    MESSAGE_SYSTEM_SERVER("messageSystem"),
    FRONTEND_SERVICE_CLIENT("frontendService"),
    DATABASE_SERVICE_CLIENT("databaseService");

    private final String value;

    TargetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

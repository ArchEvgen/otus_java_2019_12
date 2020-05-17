package ru.otus.hw16.messagesystem;

public enum MessageType {
    ADD_CLIENT("AddClient"),
    REMOVE_CLIENT("RmClient"),
    CREATE_USER("CreateUser");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package ru.otus.hw16.messagesystem;

import ru.otus.hw16.messagesystem.remote.MsClientInfo;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    TargetType getType();

    MsClientInfo getInfo();
}

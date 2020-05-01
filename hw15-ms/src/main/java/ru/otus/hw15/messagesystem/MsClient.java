package ru.otus.hw15.messagesystem;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    ClientType getType();

    Message produceMessage(ClientType to, byte[] data, MessageType msgType);

}

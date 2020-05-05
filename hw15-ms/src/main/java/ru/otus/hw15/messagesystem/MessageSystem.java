package ru.otus.hw15.messagesystem;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(ClientType clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}


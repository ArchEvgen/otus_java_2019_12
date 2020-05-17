package ru.otus.hw16.messagesystem;

import ru.otus.hw16.messagesystem.remote.MsClientInfo;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(MsClientInfo clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();
}


package ru.otus.hw16.messagesystem.remote;

import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.messagesystem.Message;
import ru.otus.hw16.messagesystem.MessageType;
import ru.otus.hw16.messagesystem.MsClient;
import ru.otus.hw16.messagesystem.RequestHandler;
import ru.otus.hw16.messagesystem.TargetType;

public class RemoteMsClient implements MsClient {
    private static Logger logger = LoggerFactory.getLogger(RemoteMsClient.class);

    private final MsClientInfo info;

    public RemoteMsClient(MsClientInfo info) {
        this.info = info;
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        throw new RuntimeException("RemoteMsClient not support addHandler method");
    }

    @Override
    public boolean sendMessage(Message msg) {
        throw new RuntimeException("RemoteMsClient not support sendMessage method");
    }

    @Override
    public void handle(Message msg) {
        try {
            try (Socket clientSocket = new Socket(info.getHost(), info.getPort())) {
                var out = new ObjectOutputStream(clientSocket.getOutputStream());
                logger.info("sending to server {}", msg);
                out.writeObject(msg);
            }

        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    @Override
    public TargetType getType() {
        return info.getType();
    }

    @Override
    public MsClientInfo getInfo() {
        return info;
    }
}

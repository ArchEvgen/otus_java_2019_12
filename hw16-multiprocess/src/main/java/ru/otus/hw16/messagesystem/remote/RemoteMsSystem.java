package ru.otus.hw16.messagesystem.remote;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.messagesystem.Message;
import ru.otus.hw16.messagesystem.MessageSystem;
import ru.otus.hw16.messagesystem.MessageType;
import ru.otus.hw16.messagesystem.MsClient;
import ru.otus.hw16.messagesystem.Serializer;
import ru.otus.hw16.messagesystem.TargetType;

import static java.util.List.of;

public class RemoteMsSystem implements MessageSystem {
    private static Logger logger = LoggerFactory.getLogger(RemoteMsSystem.class);
    private final String serverHost;
    private final int serverPort;
    private final Serializer serializer;
    private final Map<TargetType, MsClient> clientMap = new ConcurrentHashMap<>();

    public RemoteMsSystem(String serverHost, int serverPort, Serializer serializer) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.serializer = serializer;
    }

    @Override
    public void addClient(MsClient msClient) {
        var msg = new Message(
                msClient.getInfo(),
                MsClientInfo.anyOf(TargetType.MESSAGE_SYSTEM_SERVER),
                serializer.serialize(msClient.getInfo()),
                MessageType.ADD_CLIENT);
        if (newMessage(msg)) {
            clientMap.put(msClient.getType(), msClient);
        }
    }

    @Override
    public void removeClient(MsClientInfo clientId) {
        MsClient msClient = clientMap.remove(clientId);
        var msg = new Message(
                msClient.getInfo(),
                MsClientInfo.anyOf(TargetType.MESSAGE_SYSTEM_SERVER),
                serializer.serialize(msClient.getInfo()),
                MessageType.ADD_CLIENT);
        newMessage(msg);
    }

    @Override
    public boolean newMessage(Message msg) {
        try {
            try (Socket clientSocket = new Socket(serverHost, serverPort)) {
                var out = new ObjectOutputStream(clientSocket.getOutputStream());
                logger.info("sending to server {}", msg);
                out.writeObject(msg);
                return true;
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
        return false;
    }

    @Override
    public void dispose() throws InterruptedException {
        for (var c : List.of(clientMap.values())) {
            var mscli = (MsClient) c;
            removeClient(mscli.getInfo());
        }
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        dispose();
        callback.run();
    }

    @Override
    public void start() {

    }
}

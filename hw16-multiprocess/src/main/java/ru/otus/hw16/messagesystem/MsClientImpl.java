package ru.otus.hw16.messagesystem;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.messagesystem.remote.MsClientInfo;

public class MsClientImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    private final TargetType type;
    private final int port;
    private final MessageSystem messageSystem;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();
    private final ExecutorService msgListener;
    private final MsClientInfo info;

    public MsClientImpl(TargetType type, int port, MessageSystem messageSystem) {
        this.type = type;
        this.port = port;
        this.messageSystem = messageSystem;
        msgListener = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("msg-listener-thread-" + type.getValue());
            return thread;
        });
        this.info = new MsClientInfo(type, "localhost", port);
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public TargetType getType() {
        return type;
    }

    public void start() {
        msgListener.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("waiting for client connection");
                    try (Socket clientSocket = serverSocket.accept()) {
                        var in = new ObjectInputStream(clientSocket.getInputStream());
                        Message msg = (Message) in.readObject();
                        handle(msg);
                    }
                }
            } catch (Exception ex) {
                logger.error("error", ex);
            }
        });
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(type, msClient.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public MsClientInfo getInfo() {
        return info;
    }
}

package ru.otus.hw16.messagesystem;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.messagesystem.remote.MsClientInfo;

public final class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;

    private final int port;
    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final Map<TargetType, Map<MsClientInfo, MsClient>> clientsByType = new ConcurrentHashMap<>();
    private final Map<TargetType, AtomicLong> counters = new ConcurrentHashMap<>();

    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private Runnable disposeCallback;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });
    private final ExecutorService msgListener = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-listener-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {

        private final AtomicInteger threadNameSeq = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
            return thread;
        }
    });

    public MessageSystemImpl(int port) {
        this.port = port;
        start();
    }

    @Override
    public void start() {
        msgProcessor.submit(this::processMessages);
        msgListener.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("waiting for client connection");
                    try (Socket clientSocket = serverSocket.accept()) {
                        var in = new ObjectInputStream(clientSocket.getInputStream());
                        Message msg = (Message) in.readObject();
                        newMessage(msg);
                    }
                }
            } catch (Exception ex) {
                logger.error("error", ex);
            }
        });
    }

    public int currentQueueSize() {
        return messageQueue.size();
    }

    @Override
    public void addClient(MsClient msClient) {
        logger.info("new client:{}", msClient.getType());
        counters.putIfAbsent(msClient.getType(), new AtomicLong(0));
        clientsByType.putIfAbsent(msClient.getType(), new ConcurrentHashMap<>());
        clientsByType.get(msClient.getType()).put(msClient.getInfo(), msClient);
    }

    @Override
    public void removeClient(MsClientInfo info) {
        MsClient removedClient = clientsByType.get(info.getType()).remove(info);
        if (removedClient == null) {
            logger.warn("client not found: {}", info);
        } else {
            logger.info("removed client:{}", removedClient);
        }
    }

    @Override
    public boolean newMessage(Message msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            logger.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    @PreDestroy
    public void dispose() throws InterruptedException {
        logger.info("now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    private MsClient findClientToProcess(Message msg) {
        Map<MsClientInfo, MsClient> clients = clientsByType.get(msg.getTo().getType());
        if (clients == null) {
            return null;
        }
        if (msg.getTo().isAbstract()) {
            var count = counters.get(msg.getTo().getType()).incrementAndGet();
            List<MsClient> list = List.copyOf(clients.values());
            return list.get((int)(count % list.size()));
        }
        return clients.get(msg.getTo());
    }

    private void processMessages() {
        logger.info("msgProcessor started, {}", currentQueueSize());
        while (runFlag.get() || !messageQueue.isEmpty()) {
            try {
                Message msg = messageQueue.take();
                if (msg == Message.VOID_MESSAGE) {
                    logger.info("received the stop message");
                } else {
                    MsClient clientTo = findClientToProcess(msg);
                    if (clientTo == null) {
                        logger.warn("client not found");
                    } else {
                        msgHandler.submit(() -> handleMessage(clientTo, msg));
                    }
                }
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if (disposeCallback != null) {
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
        logger.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        logger.info("msgHandler has been shut down");
    }


    private void handleMessage(MsClient msClient, Message msg) {
        try {
            msClient.handle(msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("message:{}", msg);
        }
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.VOID_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.VOID_MESSAGE);
        }
    }

}

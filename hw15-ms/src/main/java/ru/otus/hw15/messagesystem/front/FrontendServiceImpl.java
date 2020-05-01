package ru.otus.hw15.messagesystem.front;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.hw15.messagesystem.ClientType;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.MsClient;
import ru.otus.hw15.messagesystem.Serializer;
import ru.otus.hw15.model.User;

@Service
public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final Serializer serializer;

    public FrontendServiceImpl(@Qualifier("FrontMsClient") MsClient msClient, Serializer serializer) {
        this.msClient = msClient;
        this.serializer = serializer;
    }

    @Override
    public void createUser(User user, Consumer<User> dataConsumer) {
        Message outMsg = msClient.produceMessage(ClientType.DATABASE_SERVICE_CLIENT, serializer.serialize(user), MessageType.CREATE_USER);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            logger.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }
}

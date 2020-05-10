package ru.otus.hw16.messagesystem.handlers;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.otus.hw16.messagesystem.Message;
import ru.otus.hw16.messagesystem.MessageSystemConfig;
import ru.otus.hw16.messagesystem.RequestHandler;
import ru.otus.hw16.messagesystem.Serializer;
import ru.otus.hw16.messagesystem.front.FrontendService;
import ru.otus.hw16.model.User;

@Service
@ConditionalOnProperty(prefix = "message-system", name = "type", havingValue = MessageSystemConfig.FRONT_TYPE)
public class CreateUserResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserResponseHandler.class);

    private final FrontendService frontendService;
    private final Serializer serializer;

    public CreateUserResponseHandler(FrontendService frontendService, Serializer serializer) {
        this.frontendService = frontendService;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            User userData = serializer.deserialize(msg.getPayload(), User.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, User.class).ifPresent(consumer -> consumer.accept(userData));

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}

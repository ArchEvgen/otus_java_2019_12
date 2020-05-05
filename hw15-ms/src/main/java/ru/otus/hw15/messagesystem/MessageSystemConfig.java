package ru.otus.hw15.messagesystem;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw15.messagesystem.front.FrontendService;
import ru.otus.hw15.messagesystem.handlers.CreateUserRequestHandler;
import ru.otus.hw15.messagesystem.handlers.CreateUserResponseHandler;
import ru.otus.hw15.services.UserService;

@Configuration
public class MessageSystemConfig {
    @Bean("FrontMsClient")
    public MsClient FrontMsClient(MessageSystem messageSystem) {
        var client = new MsClientImpl(ClientType.FRONTEND_SERVICE_CLIENT, messageSystem);
        messageSystem.addClient(client);
        return client;
    }

    @Bean("DbMsClient")
    public MsClient DbMsClient(MessageSystem messageSystem) {
        var client = new MsClientImpl(ClientType.DATABASE_SERVICE_CLIENT, messageSystem);
        messageSystem.addClient(client);
        return client;
    }

    @Bean
    public CreateUserResponseHandler CreateUserResponseHandler(FrontendService frontendService, Serializer serializer,
                                                               @Qualifier("FrontMsClient") MsClient msClient) {
        var handler = new CreateUserResponseHandler(frontendService, serializer);
        msClient.addHandler(MessageType.CREATE_USER, handler);
        return handler;
    }

    @Bean
    public CreateUserRequestHandler CreateUserRequestHandler(UserService userService, Serializer serializer,
                                                             @Qualifier("DbMsClient") MsClient msClient) {
        var handler = new CreateUserRequestHandler(userService, serializer);
        msClient.addHandler(MessageType.CREATE_USER, handler);
        return handler;
    }
}

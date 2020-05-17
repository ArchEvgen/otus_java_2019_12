package ru.otus.hw16.messagesystem;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw16.messagesystem.front.FrontendService;
import ru.otus.hw16.messagesystem.handlers.CreateUserRequestHandler;
import ru.otus.hw16.messagesystem.handlers.CreateUserResponseHandler;
import ru.otus.hw16.messagesystem.remote.RemoteMsSystem;
import ru.otus.hw16.services.UserService;

@Configuration
@EnableConfigurationProperties(MessageSystemConfigurationProperties.class)
public class MessageSystemConfig {
    public final static String FRONT_TYPE = "front";
    public final static String SRV_TYPE = "srv";
    public final static String DB_TYPE = "db";

    @Autowired
    private MessageSystemConfigurationProperties properties;

    @Bean("FrontMsClient")
    @ConditionalOnProperty(prefix = "message-system", name = "type", havingValue = FRONT_TYPE)
    public MsClient FrontMsClient(MessageSystem messageSystem) {
        var client = new MsClientImpl(TargetType.FRONTEND_SERVICE_CLIENT, properties.getPort(), messageSystem);
        client.start();
        messageSystem.addClient(client);
        return client;
    }

    @Bean("DbMsClient")
    @ConditionalOnProperty(prefix = "message-system", name = "type", havingValue = DB_TYPE)
    public MsClient DbMsClient(MessageSystem messageSystem) {
        var client = new MsClientImpl(TargetType.DATABASE_SERVICE_CLIENT, properties.getPort(), messageSystem);
        client.start();
        messageSystem.addClient(client);
        return client;
    }

    @Bean
    @ConditionalOnProperty(prefix = "message-system", name = "type", havingValue = FRONT_TYPE)
    public CreateUserResponseHandler CreateUserResponseHandler(FrontendService frontendService, Serializer serializer,
                                                               @Qualifier("FrontMsClient") MsClient msClient) {
        var handler = new CreateUserResponseHandler(frontendService, serializer);
        msClient.addHandler(MessageType.CREATE_USER, handler);
        return handler;
    }

    @Bean
    @ConditionalOnProperty(prefix = "message-system", name = "type", havingValue = DB_TYPE)
    public CreateUserRequestHandler CreateUserRequestHandler(UserService userService, Serializer serializer,
                                                             @Qualifier("DbMsClient") MsClient msClient) {
        var handler = new CreateUserRequestHandler(userService, serializer);
        msClient.addHandler(MessageType.CREATE_USER, handler);
        return handler;
    }

    @Bean
    public MessageSystem MessageSystem(Serializer serializer, AppRunner appRunner) throws IOException {
        if (SRV_TYPE.equals(properties.getType())) {
            var ms = new MessageSystemImpl(properties.getMsServerPort());
            var msc = new MsSelfClient(ms, serializer, properties.getMsServerPort());
            ms.addClient(msc);
            appRunner.runApp(properties);
            return ms;
        }
        return new RemoteMsSystem("localhost", properties.getMsServerPort(), serializer);
    }
}

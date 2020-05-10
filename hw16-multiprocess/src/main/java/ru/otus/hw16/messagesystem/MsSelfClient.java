package ru.otus.hw16.messagesystem;

import ru.otus.hw16.messagesystem.remote.MsClientInfo;
import ru.otus.hw16.messagesystem.remote.RemoteMsClient;

public class MsSelfClient implements MsClient {
    private final MessageSystem messageSystem;
    private final Serializer serializer;
    private final MsClientInfo info;

    public MsSelfClient(MessageSystem messageSystem, Serializer serializer, int port) {
        this.messageSystem = messageSystem;
        this.serializer = serializer;
        this.info = new MsClientInfo(getType(), "localhost", port);
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        throw new RuntimeException("MsSelfClient not support addHandler method");
    }

    @Override
    public boolean sendMessage(Message msg) {
        throw new RuntimeException("MsSelfClient not support sendMessage method");
    }

    @Override
    public void handle(Message msg) {
        if (msg.getType().equals(MessageType.ADD_CLIENT.getValue())) {
            MsClientInfo info = serializer.deserialize(msg.getPayload(), MsClientInfo.class);
            var msClient = new RemoteMsClient(info);
            messageSystem.addClient(msClient);
        } else if (msg.getType().equals(MessageType.REMOVE_CLIENT.getValue())) {
            MsClientInfo info = serializer.deserialize(msg.getPayload(), MsClientInfo.class);
            messageSystem.removeClient(info);
        }
    }

    @Override
    public TargetType getType() {
        return TargetType.MESSAGE_SYSTEM_SERVER;
    }

    @Override
    public MsClientInfo getInfo() {
        return info;
    }
}

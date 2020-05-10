package ru.otus.hw16.messagesystem.remote;

import java.io.Serializable;
import java.util.Objects;

import ru.otus.hw16.messagesystem.TargetType;

public class MsClientInfo implements Serializable {
    private final TargetType type;
    private final String host;
    private final int port;

    public MsClientInfo(TargetType type, String host, int port) {
        this.port = port;
        this.host = host;
        this.type = type;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public boolean isAbstract() {
        return host == null;
    }

    public TargetType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("MsClientInfo(%s, %s, %s)", type, host, port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, host, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MsClientInfo that = (MsClientInfo) o;
        return port == that.port &&
                type == that.type &&
                Objects.equals(host, that.host);
    }

    public static MsClientInfo anyOf(TargetType type) {
        return new MsClientInfo(type, null, 0);
    }
}

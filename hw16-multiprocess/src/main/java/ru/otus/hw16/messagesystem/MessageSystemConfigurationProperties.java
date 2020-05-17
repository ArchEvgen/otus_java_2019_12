package ru.otus.hw16.messagesystem;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "message-system", ignoreUnknownFields = false)
public class MessageSystemConfigurationProperties {
    private int msServerPort;
    private String type;
    private int port;
    private List<Integer> dbPorts;
    private List<Integer> frontPorts;
    private List<Integer> frontHttpPorts;

    public int getMsServerPort() {
        return msServerPort;
    }

    public void setMsServerPort(int msServerPort) {
        this.msServerPort = msServerPort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Integer> getDbPorts() {
        return dbPorts;
    }

    public void setDbPorts(List<Integer> dbPorts) {
        this.dbPorts = dbPorts;
    }

    public List<Integer> getFrontPorts() {
        return frontPorts;
    }

    public void setFrontPorts(List<Integer> frontPorts) {
        this.frontPorts = frontPorts;
    }

    public List<Integer> getFrontHttpPorts() {
        return frontHttpPorts;
    }

    public void setFrontHttpPorts(List<Integer> frontHttpPorts) {
        this.frontHttpPorts = frontHttpPorts;
    }
}

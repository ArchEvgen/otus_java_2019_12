package ru.otus.hw15.messagesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Serializer {
    private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

    public byte[] serialize(Object data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(data);
            os.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("Serialization error, data:" + data, e);
            throw new RuntimeException("Serialization error:" + e.getMessage());
        }
    }

    public <T> T deserialize(byte[] data, Class<T> classOfT) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream is = new ObjectInputStream(bis)) {
            Object obj = is.readObject();
            return classOfT.cast(obj);
        } catch (Exception e) {
            logger.error("DeSerialization error, classOfT:" + classOfT, e);
            throw new RuntimeException("DeSerialization error:" + e.getMessage());
        }
    }
}

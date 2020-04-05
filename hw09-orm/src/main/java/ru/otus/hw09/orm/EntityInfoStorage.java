package ru.otus.hw09.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInfoStorage {
    private final Map<Class, EntityInfo> cache = new HashMap<>();

    public EntityInfo getEntityInfo(Class entityClass) {
        EntityInfo info = cache.get(entityClass);
        if (info == null) {
            List<Field> keyField = new ArrayList<>();
            List<Field> fields = new ArrayList<>();
            var objClass = entityClass;
            while (!objClass.equals(Object.class)) {
                for (Field field : objClass.getDeclaredFields()) {
                    if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
                        continue;
                    }
                    if (field.getDeclaredAnnotation(Id.class) != null) {
                        keyField.add(field);
                    } else {
                        fields.add(field);
                    }
                }
                objClass = objClass.getSuperclass();
            }
            if (keyField.size() != 1) {
                throw new JdbcMapperException(String.format("For entity %s found %s fields with Id annotation",
                        entityClass.getName(), keyField.size()));
            }
            info = new EntityInfo(entityClass, entityClass.getSimpleName().toLowerCase(), keyField.get(0),
                    Collections.unmodifiableList(fields));
            cache.put(entityClass, info);
        }
        return info;
    }
}

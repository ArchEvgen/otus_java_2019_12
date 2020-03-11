package ru.otus.hw08;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DiyGson {
    public String toJson(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof Enum) {
            return stringToJson(((Enum)obj).name());
        } else if (obj instanceof String) {
            return stringToJson(obj);
        } else if (obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Map) {
            return mapToJson((Map<Object, Object>)obj);
        } else if (obj instanceof Collection) {
            return collectionToJson((Collection<Object>)obj);
        } else if (obj.getClass().isArray()) {
            return arrayToJson(obj);
        } else {
            return objectToJson(obj);
        }
    }

    private String arrayToJson(Object obj) {
        var result = "[";
        var length = Array.getLength(obj);
        for (int i = 0; i < length; ) {
            result += toJson(Array.get(obj, i));
            if (++i < length) {
                result += ",";
            }
        }
        result += "]";
        return result;
    }

    private String stringToJson(Object obj) {
        return String.format("\"%s\"", obj);
    }

    private String objectToJson(Object obj) {
        var mapView = new HashMap();
        var objClass = obj.getClass();
        while (!objClass.getName().equals(Object.class.getName())) {
            for (var field : objClass.getDeclaredFields()) {
                if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    mapView.put(field.getName(), field.get(obj));
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    System.out.println(e);
                }
            }
            objClass = objClass.getSuperclass();
        }
        return mapToJson((Map<Object, Object>)mapView);
    }

    private String collectionToJson(Collection<Object> obj) {
        return "[" + obj.stream().map(x -> toJson(x)).collect(Collectors.joining(",")) + "]";
    }

    private String mapToJson(Map<Object, Object> obj) {
        return "{" + obj.entrySet().stream().map(e -> stringToJson(e.getKey()) + ":" + toJson(e.getValue()))
            .collect(Collectors.joining(",")) + "}";
    }
}

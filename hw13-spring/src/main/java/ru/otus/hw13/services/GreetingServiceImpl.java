package ru.otus.hw13.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GreetingServiceImpl implements GreetingService {

    @Override
    public Map<String, String> sayHello(String name) {
        Map<String, String> map = new HashMap<>();
        map.put(name, "Hello, " + name);
        return map;
    }
}

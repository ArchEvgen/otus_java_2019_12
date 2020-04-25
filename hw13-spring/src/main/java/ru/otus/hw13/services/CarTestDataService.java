package ru.otus.hw13.services;

import org.springframework.stereotype.Service;

@Service("CarTestDataService")
public class CarTestDataService implements TestDataService {
    @Override
    public void fillDb() {
        throw new RuntimeException("Cars not implemented yet");
    }
}

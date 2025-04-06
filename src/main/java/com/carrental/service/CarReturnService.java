package com.carrental.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CarReturnService {
    private static final Logger returnLogger = LoggerFactory.getLogger(CarReturnService.class);

    public void returnCar(String user, String carModel) {
        returnLogger.info("Car returned: User={} | Car={}", user, carModel);
    }
}

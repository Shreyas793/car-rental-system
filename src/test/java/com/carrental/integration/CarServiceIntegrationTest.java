package com.carrental.integration;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import com.carrental.service.CarService;
import com.carrental.service.impl.CarServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class CarServiceIntegrationTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void testAddAndGetCar() {
        CarService carService = new CarServiceImpl(carRepository);

        Car car = new Car("Hyundai", "i20", 2022, 800.0, true);
        carService.addCar(car);

        List<Car> availableCars = carService.getAvailableCars();
        assertFalse(availableCars.isEmpty());
        assertEquals("Hyundai", availableCars.get(0).getMake());
    }
}

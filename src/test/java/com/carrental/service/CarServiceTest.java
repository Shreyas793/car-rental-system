package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import com.carrental.service.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository;
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarServiceImpl(carRepository);
    }

    @Test
    void testAddCar() {
        Car car = new Car("Toyota", "Camry", 2022, 1000, true);
        when(carRepository.save(car)).thenReturn(car);

        Car saved = carService.addCar(car);

        assertNotNull(saved);
        assertEquals("Camry", saved.getModel());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testGetAvailableCars() {
        Car car1 = new Car("Toyota", "Camry", 2022, 1000, true);
        Car car2 = new Car("Honda", "Civic", 2023, 1200, true);

        when(carRepository.findByAvailableTrue()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carService.getAvailableCars();

        assertNotNull(cars);
        assertEquals(0, cars.size());
        assertTrue(cars.stream().allMatch(Car::isAvailable));
    }

    @Test
    void testUpdateCar() {
        Car existing = new Car("Toyota", "Corolla", 2021, 900, true);
        existing.setId(1L);

        Car updateRequest = new Car("Toyota", "Supra", 2023, 1500, true);

        Car updatedCar = new Car("Toyota", "Supra", 2023, 1500, true);
        updatedCar.setId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(carRepository.save(any(Car.class))).thenReturn(updatedCar);

        Car result = carService.updateCar(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Supra", result.getModel());
        assertEquals(1500, result.getRentalPrice());
    }
}

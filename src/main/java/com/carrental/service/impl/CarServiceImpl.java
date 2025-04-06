package com.carrental.service.impl;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import com.carrental.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    // ✅ Use fully qualified logger name that matches logback config
    private static final Logger transactionLogger = LoggerFactory.getLogger("com.carrental.transaction");

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public Car addCar(Car car) {
        Car savedCar = carRepository.save(car);
        transactionLogger.info("Car Added - ID: {} | Make: {} | Model: {} | Year: {} | Price: ${} | Available: {}",
                savedCar.getId(), savedCar.getMake(), savedCar.getModel(), savedCar.getYear(),
                savedCar.getRentalPrice(), savedCar.isAvailable());
        return savedCar;
    }

    @Override
    @Transactional
    public Car updateCar(Long id, Car updatedCar) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + id));

        car.setMake(updatedCar.getMake());
        car.setModel(updatedCar.getModel());
        car.setYear(updatedCar.getYear());
        car.setRentalPrice(updatedCar.getRentalPrice());
        car.setAvailable(updatedCar.isAvailable());

        Car savedCar = carRepository.save(car);
        transactionLogger.info("Car Updated - ID: {} | Make: {} | Model: {} | Year: {} | Price: ${} | Available: {}",
                savedCar.getId(), savedCar.getMake(), savedCar.getModel(), savedCar.getYear(),
                savedCar.getRentalPrice(), savedCar.isAvailable());

        return savedCar;
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found with ID: " + id);
        }

        carRepository.deleteById(id);
        transactionLogger.info("Car Deleted - ID: {}", id);
    }

    @Override
    public List<Car> getAvailableCars() {
        return carRepository.findByAvailable(true);
    }
}

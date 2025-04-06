package com.carrental.service;

import com.carrental.entity.Car;
import java.util.List;

public interface CarService {
    Car addCar(Car car);
    Car updateCar(Long id, Car car);
    void deleteCar(Long id);
    List<Car> getAvailableCars();
    
 

}

// File: CarController.java
package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    // ✅ Only ADMIN can add cars
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.addCar(car));
    }

    // ✅ Only ADMIN can update cars
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    // ✅ Only ADMIN can delete cars
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ ADMIN or USER can view available cars
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }
}

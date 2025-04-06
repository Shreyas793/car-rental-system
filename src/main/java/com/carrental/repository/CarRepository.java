package com.carrental.repository;

import com.carrental.dto.CarReportDTO;
import com.carrental.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // ✅ Fetch available cars (DTO)
    @Query("SELECT new com.carrental.dto.CarReportDTO(c.id, c.make, c.model, c.year, c.available, c.rentalPrice) FROM Car c WHERE c.available = true")
    List<CarReportDTO> findAvailableCars();

    // ✅ Fetch rented cars (DTO)
    @Query("SELECT new com.carrental.dto.CarReportDTO(c.id, c.make, c.model, c.year, c.available, c.rentalPrice) FROM Car c WHERE c.available = false")
    List<CarReportDTO> findRentedCars();

    // ✅ Fetch available cars with pagination
    @Query("SELECT new com.carrental.dto.CarReportDTO(c.id, c.make, c.model, c.year, c.available, c.rentalPrice) FROM Car c WHERE c.available = true")
    Page<CarReportDTO> findAvailableCars(Pageable pageable);

    // ✅ Fetch rented cars with pagination
    @Query("SELECT new com.carrental.dto.CarReportDTO(c.id, c.make, c.model, c.year, c.available, c.rentalPrice) FROM Car c WHERE c.available = false")
    Page<CarReportDTO> findRentedCarsPaged(Pageable pageable);
    
    List<Car> findByAvailable(boolean available);
    
    List<Car> findByAvailableTrue();
    
}

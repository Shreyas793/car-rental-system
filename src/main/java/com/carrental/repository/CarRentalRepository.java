package com.carrental.repository;

import com.carrental.entity.CarRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
    
    CarRental findByBillingId(Long billingId);

    // Add this method to fix your issue
    Optional<CarRental> findByIdAndReturnedFalse(Long id);
}

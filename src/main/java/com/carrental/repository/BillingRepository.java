package com.carrental.repository;

import com.carrental.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    
    // ✅ Find Billing record by Reservation ID
    Optional<Billing> findByReservationId(Long reservationId);

    // ✅ Find Billing record by Transaction ID
    Optional<Billing> findByTransactionId(String transactionId);
}

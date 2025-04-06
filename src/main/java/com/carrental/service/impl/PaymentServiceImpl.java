package com.carrental.service.impl;

import com.carrental.entity.Billing;
import com.carrental.entity.CarRental;
import com.carrental.entity.Reservation;
import com.carrental.repository.BillingRepository;
import com.carrental.repository.CarRentalRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger transactionLogger = LoggerFactory.getLogger("com.carrental.transaction");
    private static final Logger carRentalLogger = LoggerFactory.getLogger("com.carrental.rental");
    private static final Logger paymentLogger = LoggerFactory.getLogger("com.carrental.payment");

    private final BillingRepository billingRepository;
    private final CarRentalRepository carRentalRepository;
    private final ReservationRepository reservationRepository;

    public PaymentServiceImpl(BillingRepository billingRepository, CarRentalRepository carRentalRepository, ReservationRepository reservationRepository) {
        this.billingRepository = billingRepository;
        this.carRentalRepository = carRentalRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public Billing processPayment(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found for ID: " + reservationId));

        String transactionId = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now();
        double amount = calculateTotalAmount(reservation);

        Billing billing = new Billing();
        billing.setReservation(reservation);
        billing.setTransactionId(transactionId);
        billing.setTotalAmount(amount);
        billing.setPaymentStatus("PAID");
        billing.setPaymentDate(timestamp);
        billingRepository.save(billing);

        // ✅ Payment Log
        paymentLogger.info("Payment Successful - TxID: {} | UserID: {} | Amount: ${} | CarID: {}",
                transactionId, reservation.getUser().getId(), amount, reservation.getCar().getId());

        CarRental rental = new CarRental();
        rental.setBilling(billing);
        rental.setCar(reservation.getCar());
        rental.setUser(reservation.getUser());
        rental.setRentalStartTime(timestamp);
        rental.setStatus("RENTED");
        carRentalRepository.save(rental);

        // ✅ Rental Log
        carRentalLogger.info("Rental Confirmed - UserID: {} | CarID: {} | StartTime: {} | Status: {}",
                rental.getUser().getId(), rental.getCar().getId(), rental.getRentalStartTime(), rental.getStatus());

        return billing;
    }

    @Override
    public Billing getBillingDetails(String transactionId) {
        return billingRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Billing record not found for Transaction ID: " + transactionId));
    }

    private double calculateTotalAmount(Reservation reservation) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        double dailyRate = reservation.getCar().getRentalPrice();
        return days * dailyRate;
    }
}

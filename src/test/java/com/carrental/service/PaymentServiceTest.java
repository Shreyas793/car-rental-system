package com.carrental.service;

import com.carrental.entity.*;
import com.carrental.repository.*;
import com.carrental.service.impl.PaymentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private BillingRepository billingRepo;
    private ReservationRepository reservationRepo;
    private CarRentalRepository rentalRepo;
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setup() {
        billingRepo = mock(BillingRepository.class);
        reservationRepo = mock(ReservationRepository.class);
        rentalRepo = mock(CarRentalRepository.class);
        paymentService = new PaymentServiceImpl(billingRepo, rentalRepo, reservationRepo);
    }

    @Test
    void testProcessPayment() {
        User user = new User();
        user.setId(1L);

        Car car = new Car("Tata", "Nexon", 2022, 1200, true);
        car.setId(10L);

        Reservation reservation = new Reservation();
        reservation.setId(100L);
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(3));

        when(reservationRepo.findById(100L)).thenReturn(Optional.of(reservation));

        paymentService.processPayment(100L);

        verify(billingRepo, times(1)).save(any(Billing.class));
        verify(rentalRepo, times(1)).save(any(CarRental.class));
    }
}

package com.carrental.controller;

import com.carrental.dto.ReservationDTO;
import com.carrental.entity.Reservation;
import com.carrental.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations/user")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // USER: Book a car (Only Users can book)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/book")
    public ResponseEntity<Reservation> bookCar(@RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(reservationService.bookCar(reservationDTO));
    }

    // USER: View their reservations (Only Users can view their own reservations)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    // USER: Cancel reservation (Only Users can cancel their own reservations)
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("Reservation canceled successfully");
    }
}

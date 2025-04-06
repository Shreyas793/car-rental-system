package com.carrental.service;

import com.carrental.dto.ReservationDTO;
import com.carrental.entity.Car;
import com.carrental.entity.Reservation;
import com.carrental.entity.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private static final Logger transactionLogger = LoggerFactory.getLogger("transactionLogger");

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, CarRepository carRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    public Reservation bookCar(ReservationDTO reservationDTO) {
        // Get User and Car from Database
        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(reservationDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for reservation");
        }

        // Calculate total cost
        long days = ChronoUnit.DAYS.between(reservationDTO.getStartDate(), reservationDTO.getEndDate());
        double totalCost = days * car.getRentalPrice();

        // Create Reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setTotalCost(totalCost);

        // Mark car as unavailable
        car.setAvailable(false);
        carRepository.save(car);

        Reservation savedReservation = reservationRepository.save(reservation);

        // ✅ Log reservation creation
        transactionLogger.info("Reservation Created - ReservationID: {} | UserID: {} | CarID: {} | StartDate: {} | EndDate: {} | TotalCost: ${}",
                savedReservation.getId(), user.getId(), car.getId(), savedReservation.getStartDate(), savedReservation.getEndDate(), totalCost);

        return savedReservation;
    }

    public List<Reservation> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();

            // Mark car as available again
            Car car = reservation.getCar();
            car.setAvailable(true);
            carRepository.save(car);

            reservationRepository.delete(reservation);

            // ✅ Log reservation cancellation
            transactionLogger.info("Reservation Canceled - ReservationID: {} | UserID: {} | CarID: {} | Refund: ${}",
                    reservation.getId(), reservation.getUser().getId(), reservation.getCar().getId(), reservation.getTotalCost());

        } else {
            throw new RuntimeException("Reservation not found");
        }
    }
}

package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.CarRental;
import com.carrental.exception.CarRentalNotFoundException;
import com.carrental.repository.CarRepository;
import com.carrental.repository.CarRentalRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CarRentalService {

    private static final Logger logger = LoggerFactory.getLogger(CarRentalService.class);

    @Autowired
    private CarRentalRepository rentalRepo;

    @Autowired
    private CarRepository carRepo;

    public CarRental returnCar(Long rentalId, LocalDate actualReturnDate) {
        logger.info("Processing return for rental ID: {}", rentalId);

        CarRental rental = rentalRepo.findByIdAndReturnedFalse(rentalId)
                .orElseThrow(() -> {
                    logger.warn("No active rental found with ID: {}", rentalId);
                    return new CarRentalNotFoundException("Active rental not found");
                });

        rental.setReturnDate(actualReturnDate);
        rental.setReturned(true);

        LocalDate rentalDate = rental.getRentalDate();
        if (rentalDate == null) {
            logger.error("Rental date is null for rental ID: {}", rentalId);
            throw new CarRentalNotFoundException("Rental date is not set for rental ID: " + rentalId);
        }

        long daysLate = ChronoUnit.DAYS.between(rentalDate.plusDays(5), actualReturnDate);
        if (daysLate > 0) {
            double lateFee = daysLate * 20.0;
            rental.setLateFee(lateFee);
            logger.info("Late fee of ₹{} applied for {} late days", lateFee, daysLate);
        }

        Car car = rental.getCar();
        car.setAvailable(true);
        carRepo.save(car);

        logger.info("Car with ID {} marked as available", car.getId());
        return rentalRepo.save(rental);
    }

    public byte[] returnCarAndGeneratePDF(Long rentalId) {
        logger.info("Generating return PDF for rental ID: {}", rentalId);
        try {
            CarRental rental = returnCar(rentalId, LocalDate.now());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Car Return Statement", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Rental ID: " + rental.getId(), bodyFont));
            document.add(new Paragraph("User ID: " + rental.getUser().getId(), bodyFont));
            document.add(new Paragraph("Car ID: " + rental.getCar().getId(), bodyFont));
            document.add(new Paragraph("Car Model: " + rental.getCar().getModel(), bodyFont));
            document.add(new Paragraph("Rental Date: " + rental.getRentalDate(), bodyFont));
            document.add(new Paragraph("Return Date: " + rental.getReturnDate(), bodyFont));
            document.add(new Paragraph("Rental Amount: ₹" + rental.getRentalAmount(), bodyFont));
            document.add(new Paragraph("Late Fee: ₹" + rental.getLateFee(), bodyFont));
            document.add(new Paragraph("Status: Returned", bodyFont));

            document.close();

            logger.info("PDF generation successful for rental ID: {}", rentalId);
            return out.toByteArray();

        } catch (Exception e) {
            logger.error("Error during return and PDF generation: {}", e.getMessage());
            throw new RuntimeException("Error while generating return statement: Active rental not found", e);
        }
    }
}

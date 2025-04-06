package com.carrental.controller;

import com.carrental.service.CarRentalService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final CarRentalService carRentalService;

    public RentalController(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    // ✅ Return car and get PDF
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/return")
    public ResponseEntity<byte[]> returnCar(@RequestParam Long rentalId) {
        byte[] pdfBytes = carRentalService.returnCarAndGeneratePDF(rentalId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=return-statement.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

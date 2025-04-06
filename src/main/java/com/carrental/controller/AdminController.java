package com.carrental.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.entity.CarRental;
import com.carrental.service.CarRentalService;
import com.carrental.util.PdfGeneratorUtil;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CarRentalService rentalService;

    @Autowired
    private PdfGeneratorUtil pdfGeneratorUtil;

    @PostMapping("/return/{rentalId}")
    public ResponseEntity<InputStreamResource> returnCar(
            @PathVariable Long rentalId,
            @RequestParam("returnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {

        try {
            CarRental updatedRental = rentalService.returnCar(rentalId, returnDate);

            ByteArrayInputStream bis = pdfGeneratorUtil.generateReturnStatement(updatedRental);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=return-statement.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

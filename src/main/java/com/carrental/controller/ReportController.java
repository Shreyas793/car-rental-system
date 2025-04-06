package com.carrental.controller;

import com.carrental.dto.CarReportDTO;
import com.carrental.service.ReportService;
import com.carrental.service.impl.ReportExportService;
import com.lowagie.text.DocumentException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportExportService reportExportService;

    public ReportController(ReportService reportService, ReportExportService reportExportService) {
        this.reportService = reportService;
        this.reportExportService = reportExportService;
    }

    // ✅ Get available cars (List) - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/available-cars")
    public ResponseEntity<List<CarReportDTO>> getAvailableCarsList() {
        return ResponseEntity.ok(reportService.getAvailableCarsList());
    }

    // ✅ Get rented cars (List) - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rented-cars")
    public ResponseEntity<List<CarReportDTO>> getRentedCarsList() {
        return ResponseEntity.ok(reportService.getRentedCarsList());
    }

    // ✅ Get available cars with pagination - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/available-cars/paged")
    public ResponseEntity<Page<CarReportDTO>> getAvailableCarsPaged(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reportService.getAvailableCarsPaged(pageable));
    }

    // ✅ Get rented cars with pagination - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rented-cars/paged")
    public ResponseEntity<Page<CarReportDTO>> getRentedCarsPaged(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reportService.getRentedCarsPaged(pageable));
    }

    // ✅ Export available cars to PDF - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/available-cars/pdf")
    public void exportAvailableCarsToPDF(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size, 
            HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=available_cars.pdf");

        Pageable pageable = PageRequest.of(page, size);
        reportExportService.exportToPDF(reportService.getAvailableCarsPaged(pageable).getContent(), response);
    }

    // ✅ Export available cars to Excel - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/available-cars/excel")
    public void exportAvailableCarsToExcel(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size, 
            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=available_cars.xlsx");

        Pageable pageable = PageRequest.of(page, size);
        reportExportService.exportToExcel(reportService.getAvailableCarsPaged(pageable).getContent(), response);
    }
    
 // ✅ Export rented cars to PDF - Restricted to ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rented-cars/pdf")
    public void exportRentedCarsToPDF(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size, 
            HttpServletResponse response) throws IOException, DocumentException {
        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=rented_cars.pdf");

        Pageable pageable = PageRequest.of(page, size);
        reportExportService.exportToPDF(reportService.getRentedCarsPaged(pageable).getContent(), response);
    }

}

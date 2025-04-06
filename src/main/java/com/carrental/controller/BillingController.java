package com.carrental.controller;

import com.carrental.entity.Billing;
import com.carrental.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    private final PaymentService paymentService;

    public BillingController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Process Payment
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/pay/{reservationId}")
    public ResponseEntity<Billing> makePayment(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.processPayment(reservationId));
    }

    // Get Billing Details
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{transactionId}")
    public ResponseEntity<Billing> getBilling(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.getBillingDetails(transactionId));
    }
}

package com.carrental.service;

import com.carrental.entity.Billing;

public interface PaymentService {
    Billing processPayment(Long reservationId);
    Billing getBillingDetails(String transactionId);
}

package com.carrental.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public void logTransaction(String user, String action, String details) {
        logger.info("User: {} | Action: {} | Details: {}", user, action, details);
    }

    public void logError(String user, String action, String error) {
        logger.error("User: {} | Action: {} | Error: {}", user, action, error);
    }
}

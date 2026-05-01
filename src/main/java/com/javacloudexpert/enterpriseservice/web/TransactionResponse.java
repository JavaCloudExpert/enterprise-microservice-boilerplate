package com.javacloudexpert.enterpriseservice.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        LocalDateTime createdAt) {}

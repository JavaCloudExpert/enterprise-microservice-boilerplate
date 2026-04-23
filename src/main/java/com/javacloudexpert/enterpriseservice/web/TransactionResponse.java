package com.javacloudexpert.enterpriseservice.web;

import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        LocalDateTime createdAt
) {
}


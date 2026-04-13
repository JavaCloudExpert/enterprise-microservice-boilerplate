package com.javacloudexpert.enterpriseservice.application.dto;

import java.math.BigDecimal;

public record ProcessTransactionCommand(BigDecimal amount, String currency) {}

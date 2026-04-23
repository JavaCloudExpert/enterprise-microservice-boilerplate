package com.javacloudexpert.enterpriseservice.domain;

import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter // Resolves all "Field may have Lombok @Getter" warnings
public class Transaction {
    private final UUID id;
    private final BigDecimal amount;
    private final String currency;
    private final LocalDateTime createdAt;
    private TransactionStatus status;

    public Transaction(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.currency = currency;
        this.createdAt = LocalDateTime.now(); // Lombok @Getter now "accesses" this
        this.status = TransactionStatus.PENDING;
    }

    // Package-private factory method for reconstituting from persistence (bypasses creation rules)
    private Transaction(UUID id, BigDecimal amount, String currency, TransactionStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Transaction reconstitute(UUID id, BigDecimal amount, String currency,
                                           TransactionStatus status, LocalDateTime createdAt) {
        return new Transaction(id, amount, currency, status, createdAt);
    }

    public void complete() {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only PENDING transactions can be completed.");
        }
        this.status = TransactionStatus.COMPLETED;
    }

    public void fail() {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only PENDING transactions can be failed.");
        }
        this.status = TransactionStatus.FAILED;
    }
}

package com.javacloudexpert.enterpriseservice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    @DisplayName("Should create a transaction with PENDING status and valid data")
    void shouldCreateValidTransaction() {
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "USD";

        Transaction transaction = new Transaction(amount, currency);

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    @DisplayName("Should throw exception when amount is negative")
    void shouldThrowExceptionForNegativeAmount() {
        assertThatThrownBy(() -> new Transaction(new BigDecimal("-1.00"), "USD"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be positive");
    }

    @Test
    @DisplayName("Should transition from PENDING to COMPLETED successfully")
    void shouldCompletePendingTransaction() {
        Transaction transaction = new Transaction(new BigDecimal("50.00"), "USD");

        transaction.complete();

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should throw IllegalStateException when completing a non-pending transaction")
    void shouldFailToCompleteIfAlreadyCompleted() {
        Transaction transaction = new Transaction(new BigDecimal("50.00"), "USD");
        transaction.complete(); // First completion

        assertThatThrownBy(transaction::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only PENDING transactions can be completed");
    }
}

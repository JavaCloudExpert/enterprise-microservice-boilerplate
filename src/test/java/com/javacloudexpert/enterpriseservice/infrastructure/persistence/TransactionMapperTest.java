package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import com.javacloudexpert.enterpriseservice.domain.Transaction;
import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private final TransactionMapper mapper = new TransactionMapper();

    @Test
    @DisplayName("toJpaEntity - should map domain Transaction to JPA entity correctly")
    void shouldMapDomainToJpaEntity() {
        Transaction domain = Transaction.reconstitute(
                UUID.randomUUID(),
                new BigDecimal("150.00"),
                "GBP",
                TransactionStatus.PENDING,
                LocalDateTime.now()
        );

        TransactionJpaEntity jpa = mapper.toJpaEntity(domain);

        assertThat(jpa.getId()).isEqualTo(domain.getId());
        assertThat(jpa.getAmount()).isEqualByComparingTo("150.00");
        assertThat(jpa.getCurrency()).isEqualTo("GBP");
        assertThat(jpa.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(jpa.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toDomain - should map JPA entity to domain Transaction correctly")
    void shouldMapJpaEntityToDomain() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        TransactionJpaEntity jpa = new TransactionJpaEntity();
        jpa.setId(id);
        jpa.setAmount(new BigDecimal("250.75"));
        jpa.setCurrency("EUR");
        jpa.setStatus(TransactionStatus.COMPLETED);
        jpa.setCreatedAt(createdAt);

        Transaction domain = mapper.toDomain(jpa);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getAmount()).isEqualByComparingTo("250.75");
        assertThat(domain.getCurrency()).isEqualTo("EUR");
        assertThat(domain.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("toJpaEntity then toDomain - round-trip should preserve all fields")
    void shouldPreserveFieldsOnRoundTrip() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        Transaction original = Transaction.reconstitute(id, new BigDecimal("99.99"), "USD",
                TransactionStatus.FAILED, createdAt);

        Transaction roundTripped = mapper.toDomain(mapper.toJpaEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getAmount()).isEqualByComparingTo(original.getAmount());
        assertThat(roundTripped.getCurrency()).isEqualTo(original.getCurrency());
        assertThat(roundTripped.getStatus()).isEqualTo(original.getStatus());
    }
}


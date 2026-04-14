package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // This triggers our H2 MSSQL-mode configuration
class TransactionRepositoryIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("Should persist and retrieve a Transaction entity")
    void shouldPersistAndRetrieveTransaction() {
        // Given: A JPA Entity prepared for MS SQL/DB2 structure
        TransactionJpaEntity entity = new TransactionJpaEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setAmount(new BigDecimal("250.75"));
        entity.setCurrency("USD");
        entity.setStatus(TransactionStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());

        // When: Saving to the H2 (In-Memory) database
        transactionRepository.save(entity);

        // Then: Retrieve and verify data integrity
        Optional<TransactionJpaEntity> retrieved = transactionRepository.findById(id);

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getAmount()).isEqualByComparingTo("250.75");
        assertThat(retrieved.get().getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(retrieved.get().getVersion()).isEqualTo(0L); // Verifies @Version is working
    }
}

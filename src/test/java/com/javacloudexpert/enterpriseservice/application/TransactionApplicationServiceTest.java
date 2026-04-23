package com.javacloudexpert.enterpriseservice.application;

import com.javacloudexpert.enterpriseservice.application.dto.ProcessTransactionCommand;
import com.javacloudexpert.enterpriseservice.domain.Transaction;
import com.javacloudexpert.enterpriseservice.domain.TransactionNotFoundException;
import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;
import com.javacloudexpert.enterpriseservice.infrastructure.persistence.TransactionJpaEntity;
import com.javacloudexpert.enterpriseservice.infrastructure.persistence.TransactionMapper;
import com.javacloudexpert.enterpriseservice.infrastructure.persistence.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionApplicationServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionApplicationService service;

    @Test
    @DisplayName("Should execute command and persist transaction")
    void shouldExecuteAndPersist() {
        ProcessTransactionCommand command = new ProcessTransactionCommand(new BigDecimal("150.00"), "EUR");
        TransactionJpaEntity entity = new TransactionJpaEntity();
        when(mapper.toJpaEntity(any(Transaction.class))).thenReturn(entity);

        service.execute(command);

        verify(repository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Should find transaction by ID and return domain object")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        TransactionJpaEntity entity = new TransactionJpaEntity();
        Transaction expected = Transaction.reconstitute(
                id, new BigDecimal("100.00"), "USD", TransactionStatus.COMPLETED, LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expected);

        Transaction result = service.findById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should throw TransactionNotFoundException when ID does not exist")
    void shouldThrowNotFoundForUnknownId() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}


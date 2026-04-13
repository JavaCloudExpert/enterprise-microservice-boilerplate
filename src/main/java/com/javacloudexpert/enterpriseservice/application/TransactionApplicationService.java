package com.javacloudexpert.enterpriseservice.application;

import com.javacloudexpert.enterpriseservice.application.dto.ProcessTransactionCommand;
import com.javacloudexpert.enterpriseservice.domain.Transaction;
import com.javacloudexpert.enterpriseservice.infrastructure.persistence.TransactionMapper;
import com.javacloudexpert.enterpriseservice.infrastructure.persistence.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionApplicationService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    @Transactional
    @CircuitBreaker(name = "transactionService", fallbackMethod = "processFallback")
    public void execute(ProcessTransactionCommand command) {
        log.info("Processing transaction for amount: {}", command.amount());

        // 1. Create Domain Object (Business Rules validated here)
        Transaction transaction = new Transaction(command.amount(), command.currency());

        // 2. Perform Business Action
        transaction.complete();

        // 3. Persist via Infrastructure
        repository.save(mapper.toJpaEntity(transaction));
        
        log.info("Transaction {} processed successfully", transaction.getId());
    }

    // Senior Touch: Handling failures gracefully
    @SuppressWarnings("unused") // This method is intentionally left for Resilience4j fallback
    private void processFallback(ProcessTransactionCommand command, Throwable t) {
        log.error("Circuit Breaker triggered for transaction. Reason: {}", t.getMessage());
        // Here you would implement logic like:
        // - Sending to a 'Failed' queue (RabbitMQ/Kafka)
        // - Saving to a temporary DB2 staging table
        throw new RuntimeException("Service temporarily unavailable. Please try again later.");
    }
}

package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import com.javacloudexpert.enterpriseservice.domain.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public TransactionJpaEntity toJpaEntity(Transaction domain) {
        TransactionJpaEntity jpa = new TransactionJpaEntity();
        jpa.setId(domain.getId());
        jpa.setAmount(domain.getAmount());
        jpa.setCurrency(domain.getCurrency());
        jpa.setStatus(domain.getStatus());
        jpa.setCreatedAt(LocalDateTime.now());
        return jpa;
    }

    public Transaction toDomain(TransactionJpaEntity jpa) {
        return Transaction.reconstitute(
                jpa.getId(),
                jpa.getAmount(),
                jpa.getCurrency(),
                jpa.getStatus(),
                jpa.getCreatedAt()
        );
    }
}

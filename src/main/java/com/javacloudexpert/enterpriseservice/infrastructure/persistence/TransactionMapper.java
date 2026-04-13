package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import com.javacloudexpert.enterpriseservice.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionJpaEntity toJpaEntity(Transaction domain) {
        TransactionJpaEntity jpa = new TransactionJpaEntity();
        jpa.setId(domain.getId());
        jpa.setAmount(domain.getAmount());
        jpa.setCurrency(domain.getCurrency());
        jpa.setStatus(domain.getStatus());
        jpa.setCreatedAt(java.time.LocalDateTime.now());
        return jpa;
    }
    
    // Add toDomain() method here later for reading from DB
}

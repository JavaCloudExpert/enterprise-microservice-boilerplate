package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    // You can add custom DB2/MS SQL native queries here later if needed
}

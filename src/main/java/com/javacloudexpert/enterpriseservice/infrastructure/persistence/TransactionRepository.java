package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    // You can add custom DB2/MS SQL native queries here later if needed
}

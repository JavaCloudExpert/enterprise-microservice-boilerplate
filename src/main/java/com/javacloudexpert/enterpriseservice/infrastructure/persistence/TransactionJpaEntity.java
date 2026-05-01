package com.javacloudexpert.enterpriseservice.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;

import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions") // For MS SQL/DB2 compatibility
@Getter
@Setter
@NoArgsConstructor
public class TransactionJpaEntity {

    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR) // Tells Hibernate to use a standard UUID format
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 4) // Enterprise standard for Currency
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Expert Touch: Optimistic Locking for high-concurrency enterprise apps
    @Version private Long version;
}

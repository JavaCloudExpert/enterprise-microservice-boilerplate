package com.javacloudexpert.enterpriseservice.web;

import com.javacloudexpert.enterpriseservice.application.TransactionApplicationService;
import com.javacloudexpert.enterpriseservice.application.dto.ProcessTransactionCommand;
import com.javacloudexpert.enterpriseservice.domain.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "Endpoints for processing enterprise transactions")
public class TransactionController {

    private final TransactionApplicationService service;

    @PostMapping
    @Operation(summary = "Process a new transaction",
               description = "Validates business rules and persists transaction to MS SQL/DB2 via a resilient pipeline.")
    @ApiResponse(responseCode = "201", description = "Transaction processed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid transaction data")
    @ApiResponse(responseCode = "503", description = "Service unavailable (Circuit Breaker open)")
    public ResponseEntity<Void> process(@Valid @RequestBody ProcessTransactionCommand command) {
        service.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction by ID",
               description = "Retrieves a persisted transaction by its UUID.")
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    public ResponseEntity<TransactionResponse> getById(@PathVariable UUID id) {
        Transaction transaction = service.findById(id);
        return ResponseEntity.ok(new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        ));
    }
}

package com.javacloudexpert.enterpriseservice.web;

import com.javacloudexpert.enterpriseservice.application.TransactionApplicationService;
import com.javacloudexpert.enterpriseservice.application.dto.ProcessTransactionCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> process(@RequestBody ProcessTransactionCommand command) {
        service.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

package com.javacloudexpert.enterpriseservice.web;

import com.javacloudexpert.enterpriseservice.application.TransactionApplicationService;
import com.javacloudexpert.enterpriseservice.domain.Transaction;
import com.javacloudexpert.enterpriseservice.domain.TransactionNotFoundException;
import com.javacloudexpert.enterpriseservice.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionApplicationService service;

    @Test
    @DisplayName("POST /api/v1/transactions - should return 201 for valid command")
    void shouldReturn201ForValidCommand() throws Exception {
        doNothing().when(service).execute(any());

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.00,\"currency\":\"USD\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/transactions - should return 400 when amount is null")
    void shouldReturn400WhenAmountIsNull() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/transactions - should return 400 when currency is not 3 letters")
    void shouldReturn400WhenCurrencyInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.00,\"currency\":\"US\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} - should return 200 with transaction")
    void shouldReturn200WithTransaction() throws Exception {
        UUID id = UUID.randomUUID();
        Transaction transaction = Transaction.reconstitute(
                id, new BigDecimal("100.00"), "USD", TransactionStatus.COMPLETED, LocalDateTime.now());

        when(service.findById(id)).thenReturn(transaction);

        mockMvc.perform(get("/api/v1/transactions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenThrow(new TransactionNotFoundException(id));

        mockMvc.perform(get("/api/v1/transactions/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Transaction Not Found"));
    }
}







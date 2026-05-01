package com.javacloudexpert.enterpriseservice.web;

import com.javacloudexpert.enterpriseservice.domain.TransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionNotFoundException.class)
    public ProblemDetail handleNotFound(TransactionNotFoundException ex) {
        log.warn("Transaction not found: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Transaction Not Found");
        problem.setType(URI.create("https://api.javacloudexpert.com/errors/transaction-not-found"));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", details);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details);
        problem.setTitle("Validation Failed");
        problem.setType(URI.create("https://api.javacloudexpert.com/errors/validation-failed"));
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad Request");
        problem.setType(URI.create("https://api.javacloudexpert.com/errors/bad-request"));
        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Unprocessable Entity");
        problem.setType(URI.create("https://api.javacloudexpert.com/errors/invalid-state-transition"));
        return problem;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleServiceUnavailable(RuntimeException ex) {
        log.error("Service error: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable. Please try again later.");
        problem.setTitle("Service Unavailable");
        problem.setType(URI.create("https://api.javacloudexpert.com/errors/service-unavailable"));
        return problem;
    }
}


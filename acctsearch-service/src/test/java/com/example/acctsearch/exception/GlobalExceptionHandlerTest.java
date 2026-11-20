package com.example.acctsearch.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/accounts/test");
    }

    @Test
    void handleNotFound_returns404WithAccountId() {
        // Arrange
        AccountNotFoundException ex = new AccountNotFoundException("acct-1");

        // Act
        ResponseEntity<ApiError> response = handler.handleNotFound(ex, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).contains("acct-1");
        assertThat(response.getBody().path()).isEqualTo("/api/accounts/test");
        assertThat(response.getBody().timestamp()).isNotNull();
    }

    @Test
    void handleEventPublish_returns503() {
        // Arrange
        EventPublishException ex = new EventPublishException("Kafka error", new RuntimeException("cause"));

        // Act
        ResponseEntity<ApiError> response = handler.handleEventPublish(ex, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(503);
        assertThat(response.getBody().error()).isEqualTo("Service Unavailable");
    }

    @Test
    void handleValidation_returns400() {
        // Arrange
        Exception ex = new RuntimeException("Validation failed");

        // Act
        ResponseEntity<ApiError> response = handler.handleValidation(ex, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
    }

    @Test
    void handleGeneric_returns500() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<ApiError> response = handler.handleGeneric(ex, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().message()).isEqualTo("Unexpected error");
        assertThat(response.getBody().path()).isEqualTo("/api/accounts/test");
    }
}

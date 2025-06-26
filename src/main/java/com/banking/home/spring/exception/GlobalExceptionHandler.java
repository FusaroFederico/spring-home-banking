package com.banking.home.spring.exception;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// Validazione dei DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Login errato
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
    }

    // Utente non trovato
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
    }

    // Accesso negato
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accesso negato");
    }

    // Token JWT scaduto
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token scaduto");
    }

    // Firma JWT non valida
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleInvalidJwt(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token non valido");
    }

    // Errori logici
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Violazione della sicurezza
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // Vincoli DB (email unica)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email già in uso da un altro utente");
    }

    // Eccezione generica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ex.printStackTrace(); // utile per debug locale
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore imprevisto");
    }

}

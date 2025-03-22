package com.back.infrastructure.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResultResponse<Object,String>> handleNotFound(
            EntityNotFoundException exc){
        return buildErrorResponse(exc.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<Object, ValidationErrorResponse>> handleValidationError(
            MethodArgumentNotValidException exc){
        List<ValidationErrorResponse> messages = new ArrayList<>();

        for(FieldError fieldError : exc.getFieldErrors()){
            messages.add(new ValidationErrorResponse(
                    fieldError.getField(),
                    Collections.singletonList(fieldError.getDefaultMessage())
            ));
        }
        return buildValidationResponse(messages);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResultResponse<Object, ValidationErrorResponse>> handleConstraintViolation(
            ConstraintViolationException exc
    ){
        List<ValidationErrorResponse> messages = new ArrayList<>();
        for(ConstraintViolation<?> violation : exc.getConstraintViolations()){
            messages.add(new ValidationErrorResponse(
                    violation.getPropertyPath().toString(),
                    Collections.singletonList(violation.getMessage())
            ));
        }
        return buildValidationResponse(messages);
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ResultResponse<Object,String>> handleJpaSystemException(
            JpaSystemException exc
    ){
        return buildErrorResponse("System error: " + exc.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<Object,String>> handleGeneralException(
            Exception exc
    ){
        return buildErrorResponse("Internal server error: " + exc.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ResultResponse<Object,String>> handleSecurityException(
            SecurityException exc
    ) {
        return buildErrorResponse("Security error: " + exc.getMessage(),
                HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ResultResponse<Object,String>> buildErrorResponse(
            String message, HttpStatus status
    ){
        ResultResponse<Object,String> response = ResultResponse.failure(
                Collections.singletonList(message),
                status.value()
        );
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<ResultResponse<Object, ValidationErrorResponse>> buildValidationResponse(
            List<ValidationErrorResponse> messages) {
        ResultResponse<Object, ValidationErrorResponse> response =
                ResultResponse.failure(messages, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

package com.hackathon.expensetracker.exception;

import com.hackathon.expensetracker.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        ApiResponse response = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BudgetExceededException.class)
    public ResponseEntity<ApiResponse> handleBudgetExceededException(
            BudgetExceededException ex,
            WebRequest request) {
        ApiResponse response = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse> handleInvalidRequestException(
            InvalidRequestException ex,
            WebRequest request) {
        ApiResponse response = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse response = new ApiResponse(false, "Validation failed");
        response.setData(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        ApiResponse response = new ApiResponse(false, "An error occurred: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

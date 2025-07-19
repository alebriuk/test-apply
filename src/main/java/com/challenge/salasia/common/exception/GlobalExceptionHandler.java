package com.challenge.salasia.common.exception;

import com.challenge.salasia.common.dto.FieldErrorResponse;
import com.challenge.salasia.common.dto.ValidationErrorResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    List<FieldErrorResponse> errorList =
        ex.getBindingResult().getFieldErrors().stream()
            .map(err -> new FieldErrorResponse(err.getField(), err.getDefaultMessage()))
            .toList();

    return ResponseEntity.badRequest()
        .body(new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errorList));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleAll(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Server error: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}

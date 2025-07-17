package com.challenge.salasia.common;

import com.challenge.salasia.shared.FieldErrorResponse;
import com.challenge.salasia.shared.ValidationErrorResponse;
import java.util.List;
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
}

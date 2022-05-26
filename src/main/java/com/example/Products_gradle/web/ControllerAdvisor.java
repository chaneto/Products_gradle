package com.example.Products_gradle.web;

import java.time.LocalDate;
import com.example.Products_gradle.exeptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorMessage> handleValidationException(ValidationException ex) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.BAD_REQUEST.toString(),
      LocalDate.now(),
      "Validation fields errors!!!",
      ex.getMessage());
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorMessage> handleConflictException(ConflictException ex) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.CONFLICT.toString(),
      LocalDate.now(),
      "This product is already exists!!!",
      ex.getMessage());
    return new ResponseEntity<>(message, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException ex) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.NOT_FOUND.toString(),
      LocalDate.now(),
      "No such product was found!!!",
      ex.getMessage());
    return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CheckQuantityException.class)
  public ResponseEntity<ErrorMessage> handleQuantityException(CheckQuantityException ex) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.BAD_REQUEST.toString(),
      LocalDate.now(),
      "Quantity exception!!!",
      ex.getMessage());
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorMessage> handleQuantityException(HttpMessageNotReadableException ex) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.BAD_REQUEST.toString(),
      LocalDate.now(),
      "Invalid field value!!!",
      ex.getMessage());
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }
}

package com.example.Products_gradle.exceptions;

public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }
}

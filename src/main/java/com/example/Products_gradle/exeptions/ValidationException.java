package com.example.Products_gradle.exeptions;

public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }
}

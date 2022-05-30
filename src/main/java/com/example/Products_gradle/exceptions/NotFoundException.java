package com.example.Products_gradle.exceptions;

public class NotFoundException extends RuntimeException{

  public NotFoundException(String message) {
    super(message);
  }
}

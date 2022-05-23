package com.example.Products_gradle.exeptions;

public class NotFoundException extends RuntimeException{

  public NotFoundException(String message) {
    super(message);
  }
}

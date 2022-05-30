package com.example.Products_gradle.exceptions;

public class CheckQuantityException extends RuntimeException{

  public CheckQuantityException(String message) {
    super(message);
  }
}

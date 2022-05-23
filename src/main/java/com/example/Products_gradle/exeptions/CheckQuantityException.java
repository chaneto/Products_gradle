package com.example.Products_gradle.exeptions;

public class CheckQuantityException extends RuntimeException{

  public CheckQuantityException(String message) {
    super(message);
  }
}

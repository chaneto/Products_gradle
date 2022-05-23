package com.example.Products_gradle.exeptions;

import java.time.LocalDate;;

public class ErrorMessage {
  private String statusCode;
  private LocalDate timestamp;
  private String message;
  private String description;

  public ErrorMessage(String statusCode, LocalDate timestamp, String message, String description) {
    this.statusCode = statusCode;
    this.timestamp = timestamp;
    this.message = message;
    this.description = description;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

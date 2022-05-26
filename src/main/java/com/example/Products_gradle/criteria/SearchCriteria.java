package com.example.Products_gradle.criteria;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SearchCriteria {

  @NotBlank(message = "Field cannot be empty string or null!!!")
  @Size(min = 1, message = "Field length must be more than 1 character!!!")
  private String field;

  @NotBlank(message = "Operation cannot be empty string or null!!!")
  @Size(min = 1, message = "Operation length must be more than 1 character!!!")
  private String operation;

  @NotNull(message = "Value cannot be null!!!")
  private Object value;

  public SearchCriteria() {
  }

  public SearchCriteria(String field, String operation, Object value) {
    this.field = field;
    this.operation = operation;
    this.value = value;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}

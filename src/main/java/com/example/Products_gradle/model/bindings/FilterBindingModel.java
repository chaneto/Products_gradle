package com.example.ProductsGradle.model.bindings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FilterBindingModel {

  @NotBlank(message = "Field cannot be empty string or null!!!")
  @Size(min = 1, message = "Field length must be more than 1 character!!!")
  private String field;

  @NotBlank(message = "Name cannot be empty string or null!!!")
  @Size(min = 1, message = "Name length must be more than 1 character!!!")
  private String value;

  @NotBlank(message = "Operation cannot be empty string or null!!!")
  @Size(min = 1, message = "Operation length must be more than 1 character!!!")
  private String operation;

  public FilterBindingModel() {
  }

  public FilterBindingModel(String field, String value, String operation) {
    this.field = field;
    this.value = value;
    this.operation = operation;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }
}

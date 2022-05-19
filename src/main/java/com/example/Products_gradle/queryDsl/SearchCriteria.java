package com.example.ProductsGradle.queryDsl;

public class SearchCriteria {


  private String field;
  private String operation;
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

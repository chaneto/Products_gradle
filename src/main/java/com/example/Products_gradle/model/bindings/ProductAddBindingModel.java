package com.example.ProductsGradle.model.bindings;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;

import org.springframework.format.annotation.DateTimeFormat;

public class ProductAddBindingModel {



  private Long id;

  @Expose
  @NotBlank(message = "Name cannot be empty string or null!!!")
  @Size(min = 1, message = "Name length must be more than 1 character!!!")
  private String name;

  @Expose
  @NotBlank(message = "Category cannot be empty string or null!!!")
  @Size(min = 1, message = "Category length must be more than 1 character!!!")
  private String category;

  @Expose
  @NotBlank(message = "Description cannot be empty string or null!!!")
  @Size(min = 10, message = "Description length must be more than 10 characters!!!")
  private String description;

  @Expose
  @NotNull(message = "Quantity cannot be null!!!")
  @DecimalMin(value = "0", message = "Тhe quantity cannot be a negative value!!!")
  private BigDecimal quantity;

  @Expose
  @JsonFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "The create date cannot be null!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @PastOrPresent(message = "The create date cannot be in the future!")
  private LocalDate createdDate;

  @Expose
  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @PastOrPresent(message = "The modified date cannot be in the future!")
  private LocalDate lastModifiedDate;

  public ProductAddBindingModel() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDate getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(LocalDate lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}

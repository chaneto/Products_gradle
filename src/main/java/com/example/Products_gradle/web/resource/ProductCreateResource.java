package com.example.Products_gradle.web.resource;

import java.math.BigDecimal;
import javax.validation.constraints.*;

public class ProductCreateResource {

  @NotBlank(message = "Name cannot be empty string or null!!!")
  @Size(min = 1, message = "Name length must be more than 1 character!!!")
  private String name;

  @NotBlank(message = "Category cannot be empty string or null!!!")
  @Size(min = 1, message = "Category length must be more than 1 character!!!")
  private String category;

  @NotBlank(message = "Description cannot be empty string or null!!!")
  @Size(min = 10, message = "Description length must be more than 10 characters!!!")
  private String description;

  @NotNull(message = "Quantity cannot be null!!!")
  @DecimalMin(value = "0", message = "Тhe quantity cannot be a negative value!!!")
  private BigDecimal quantity;

  public ProductCreateResource() {
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

}

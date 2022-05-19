package com.example.Products_gradle.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;


@Entity
@Table(name = "products")
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Unique product identifier")
  private Long id;

  @Column(nullable = false, unique = true)
  @Size(min = 1)
  @ApiModelProperty(value = "Product name")
  private String name;

  @Column(nullable = false)
  @Size(min = 1)
  @ApiModelProperty(value = "The category to which the product belongs")
  private String category;

  @Column(columnDefinition = "TEXT")
  @Size(min = 10)
  @ApiModelProperty(value = "Detailed product information")
  private String description;

  @Column(nullable = false)
  @DecimalMin("0")
  @ApiModelProperty(value = "The available quantity of the product in the database")
  private BigDecimal quantity;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "created_date", nullable = false)
  @ApiModelProperty(value = "Date of creation of the product")
  private LocalDate createdDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "last_modified_date")
  @ApiModelProperty(value = "Last modification of the product")
  private LocalDate lastModifiedDate;

  public ProductEntity() {
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

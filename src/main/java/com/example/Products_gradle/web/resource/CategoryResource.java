package com.example.Products_gradle.web.resource;

public class CategoryResource {

  private String category;

  private int productsAvailable;

  public CategoryResource() {
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getProductsAvailable() {
    return productsAvailable;
  }

  public void setProductsAvailable(int productsAvailable) {
    this.productsAvailable = productsAvailable;
  }
}

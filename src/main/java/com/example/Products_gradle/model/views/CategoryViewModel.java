package com.example.ProductsGradle.model.views;

public class CategoryViewModel {

  private String category;
  private int productsAvailable;

  public CategoryViewModel() {
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

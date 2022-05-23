package com.example.Products_gradle.web.resource;

import java.util.List;

public class ProductRestResource {

  private List<ProductResource> products;
  private int totalRecords;

  public ProductRestResource() {
  }

  public ProductRestResource(
    List<ProductResource> products, int totalRecords) {
    this.products = products;
    this.totalRecords = totalRecords;
  }

  public List<ProductResource> getProducts() {
    return products;
  }

  public void setProducts(
    List<ProductResource> products) {
    this.products = products;
  }

  public int getTotalRecords() {
    return totalRecords;
  }

  public void setTotalRecords(int totalRecords) {
    this.totalRecords = totalRecords;
  }
}

package com.example.Products_gradle.web.assembler;

import java.util.ArrayList;
import java.util.List;
import com.example.Products_gradle.model.entities.Product;
import com.example.Products_gradle.web.resource.ProductCreateResource;
import com.example.Products_gradle.web.resource.ProductResource;
import org.springframework.stereotype.Component;

@Component
public class ProductAssembler {

  public Product assembleProduct(ProductCreateResource productCreateResource) {
    Product product = new Product();
    product.setName(productCreateResource.getName());
    product.setCategory(productCreateResource.getCategory());
    product.setQuantity(productCreateResource.getQuantity());
    product.setDescription(productCreateResource.getDescription());
    return product;
  }

  public ProductResource assembleProductResource(Product product) {
    ProductResource productResource = new ProductResource();
    productResource.setId(product.getId());
    productResource.setName(product.getName());
    productResource.setCategory(product.getCategory());
    productResource.setQuantity(product.getQuantity());
    productResource.setDescription(product.getDescription());
    productResource.setCreatedDate(product.getCreatedDate());
    return productResource;
  }

  public List<ProductResource> assembleProductsResource(List<Product> products) {
    List<ProductResource> productResources = new ArrayList<>();
    for (Product product : products) {
      productResources.add(assembleProductResource(product));
    }
    return productResources;
  }

}

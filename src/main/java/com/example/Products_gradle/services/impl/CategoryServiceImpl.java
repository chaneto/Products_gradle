package com.example.Products_gradle.services.impl;

import java.util.ArrayList;
import java.util.List;
import com.example.Products_gradle.repositories.ProductRepository;
import com.example.Products_gradle.services.CategoryService;
import com.example.Products_gradle.web.resource.CategoryResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {


  private final ProductRepository productRepository;

  @Autowired
  public CategoryServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }


  @Override
  public List<CategoryResource> getAllCategory() {
    List<CategoryResource> categories = new ArrayList<>();
    for (int i = 0; i < this.productRepository.getAllCategory().size(); i++) {
      CategoryResource categoryResource = new CategoryResource();
      categoryResource.setCategory(this.productRepository.getAllCategory().get(i)[0]);
      categoryResource.setProductsAvailable(
        Integer.parseInt(this.productRepository.getAllCategory().get(i)[1]));
      categories.add(categoryResource);
    }
    return categories;
  }
}

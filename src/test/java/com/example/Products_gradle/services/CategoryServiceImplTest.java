package com.example.ProductsGradle.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.ProductsGradle.model.entities.ProductEntity;
import com.example.ProductsGradle.model.views.CategoryViewModel;
import com.example.ProductsGradle.repositories.ProductRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ProductRepository productRepository;

  ProductEntity productEntity1;
  ProductEntity productEntity3;

  @Before
  public void setup() {
    this.productRepository.deleteAll();

    productEntity1 = new ProductEntity();
    productEntity1.setName("Toshiba crx");
    productEntity1.setCategory("Laptop");
    productEntity1.setDescription("Toshiba crx description...");
    productEntity1.setQuantity(BigDecimal.valueOf(33));
    productEntity1.setCreatedDate(LocalDate.of(2020, 11, 3));
    productEntity1.setLastModifiedDate(LocalDate.of(2021, 12, 3));
    this.productRepository.save(productEntity1);

    productEntity3 = new ProductEntity();
    productEntity3.setName("Apple");
    productEntity3.setCategory("Monitor");
    productEntity3.setDescription("Apple description...");
    productEntity3.setQuantity(BigDecimal.valueOf(3));
    productEntity3.setCreatedDate(LocalDate.of(2021, 3, 21));
    productEntity3.setLastModifiedDate(LocalDate.of(2022, 1, 1));
    this.productRepository.save(productEntity3);
  }

  @Test
  public void testGetAllCategory() {
    List<CategoryViewModel> categories = this.categoryService.getAllCategory();
    Assert.assertEquals(2, categories.size());
    Assert.assertEquals(categories.get(0).getCategory(), "Monitor");
    Assert.assertEquals(categories.get(0).getProductsAvailable(), 1);
    Assert.assertEquals(categories.get(1).getCategory(), "Laptop");
    Assert.assertEquals(categories.get(1).getProductsAvailable(), 1);
  }
}

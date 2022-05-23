package com.example.Products_gradle.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.example.Products_gradle.model.entities.Product;
import com.example.Products_gradle.repositories.ProductRepository;
import com.example.Products_gradle.web.resource.CategoryResource;
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

  Product product1;
  Product product3;

  @Before
  public void setup() {
    this.productRepository.deleteAll();

    product1 = new Product();
    product1.setName("Toshiba crx");
    product1.setCategory("Laptop");
    product1.setDescription("Toshiba crx description...");
    product1.setQuantity(BigDecimal.valueOf(33));
    product1.setCreatedDate(LocalDate.of(2020, 11, 3));
    product1.setLastModifiedDate(LocalDate.of(2021, 12, 3));
    this.productRepository.save(product1);

    product3 = new Product();
    product3.setName("Apple");
    product3.setCategory("Monitor");
    product3.setDescription("Apple description...");
    product3.setQuantity(BigDecimal.valueOf(3));
    product3.setCreatedDate(LocalDate.of(2021, 3, 21));
    product3.setLastModifiedDate(LocalDate.of(2022, 1, 1));
    this.productRepository.save(product3);
  }

  @Test
  public void testGetAllCategory() {
    List<CategoryResource> categories = this.categoryService.getAllCategory();
    Assert.assertEquals(2, categories.size());
    Assert.assertEquals(categories.get(0).getCategory(), "Monitor");
    Assert.assertEquals(categories.get(0).getProductsAvailable(), 1);
    Assert.assertEquals(categories.get(1).getCategory(), "Laptop");
    Assert.assertEquals(categories.get(1).getProductsAvailable(), 1);
  }
}

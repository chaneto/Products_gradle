package com.example.Products_gradle.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.Products_gradle.criteria.ProductPredicate;
import com.example.Products_gradle.exeptions.CheckQuantityException;
import com.example.Products_gradle.exeptions.ConflictException;
import com.example.Products_gradle.exeptions.NotFoundException;
import com.example.Products_gradle.exeptions.ValidationException;
import com.example.Products_gradle.model.entities.Product;
import com.example.Products_gradle.web.assembler.ProductAssembler;
import com.example.Products_gradle.web.resource.FilterResource;
import com.example.Products_gradle.web.resource.OrderCreateResource;
import com.example.Products_gradle.repositories.ProductRepository;
import com.example.Products_gradle.web.resource.ProductCreateResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceImplTest {


  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductAssembler productAssembler;

  Product product1;
  Product product3;
  Product productNotSafeINDatabase;
  ProductCreateResource productCreateResource;

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

    productNotSafeINDatabase = new Product();
    productNotSafeINDatabase.setName("Samsung");
    productNotSafeINDatabase.setCategory("TV");
    productNotSafeINDatabase.setDescription("Toshiba crx description...");
    productNotSafeINDatabase.setQuantity(BigDecimal.valueOf(1));
    productNotSafeINDatabase.setCreatedDate(LocalDate.of(2020, 11, 3));

    productCreateResource = new ProductCreateResource();
    productCreateResource.setName("Sony");
    productCreateResource.setQuantity(BigDecimal.valueOf(33));
    productCreateResource.setCategory("TV");
    productCreateResource.setDescription("Sony description...");
  }

  @Test
  public void testSeedProduct() {
    this.productRepository.deleteAll();
    Assert.assertEquals(0, this.productRepository.count());
    product1.setCreatedDate(null);
    product3.setLastModifiedDate(null);
    this.productService.seedProduct(product1);
    this.productService.seedProduct(product3);
    Assert.assertEquals(2, this.productRepository.count());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void testSeedProductWithInvalidData() {
    this.productRepository.deleteAll();
    Assert.assertEquals(0, this.productRepository.count());
    product1.setName("");
    this.productService.seedProduct(product1);
  }

  @Test
  public void getAllProductCount() {
    this.productRepository.deleteAll();
    FilterResource filterResource = new FilterResource("quantity", "5", "ge");
    Specification specification = this.productService.getAllSpecifications(List.of(filterResource));
    Assert.assertEquals(0, this.productService.getAllProductCount(specification));
    this.productRepository.save(product1);
    this.productRepository.save(product3);
    Assert.assertEquals(1, this.productService.getAllProductCount(specification));
  }

  @Test
  public void testProductIsExists() {
    Assert.assertTrue(this.productService.productIsExists("Samsung"));
  }

  @Test(expected = ConflictException.class)
  public void testProductIsExistsException() {
    String name = this.productRepository.findAll().get(0).getName();
    Assert.assertEquals(this.productService.productIsExists(name),
      "Product with name '" + name + "' is allready exests!!!");
  }

  @Test
  public void testGetById() {
    Long productId = this.productRepository.findAll().get(0).getId();
    Product product = this.productService.getById(productId);
    Assert.assertEquals(product1.getName(), product.getName());
    Assert.assertEquals(product1.getQuantity().stripTrailingZeros(),
      product.getQuantity().stripTrailingZeros());
    Assert.assertEquals(product1.getCategory(), product.getCategory());
  }

  @Test
  public void testGetByIdWithInvalidId() {
    Long productId = this.productRepository.findAll().get(0).getId();
    this.productService.deleteProductById(productId);
    Product product = this.productService.getById(productId);
    Assert.assertNull(product);
  }

  @Test
  public void testDeleteProductById() {
    Long productId = this.productRepository.findAll().get(0).getId();
    Assert.assertEquals(2, this.productRepository.count());
    this.productService.deleteProductById(productId);
    Assert.assertEquals(1, this.productRepository.count());
  }

  @Test
  public void testUpdateProduct() {
    String newName = productNotSafeINDatabase.getName();
    String newCategory = productNotSafeINDatabase.getCategory();
    BigDecimal newQuantity = productNotSafeINDatabase.getQuantity();
    Long id = this.productRepository.findAll().get(0).getId();
    productNotSafeINDatabase.setId(id);
    Assert.assertEquals("Toshiba crx", this.productService.getById(id).getName());
    this.productService.seedProduct(productNotSafeINDatabase);
    Assert.assertEquals(newName, this.productService.getById(id).getName());
    Assert.assertEquals(newCategory, this.productService.getById(id).getCategory());
    Assert.assertEquals(newQuantity.stripTrailingZeros(),
      this.productService.getById(id).getQuantity().stripTrailingZeros());
  }

  @Test
  public void testGetAllProducts() {
    List<Product> products = new ArrayList<>();
    Sort sort = null;
    FilterResource filterResource = new FilterResource("quantity", "0", "ge");
    Specification specification = this.productService.getAllSpecifications(List.of(filterResource));
    ProductPredicate predicate = new ProductPredicate();
    sort = predicate.getSorted("name", "ASC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals(this.productService.getAllProductCount(specification), products.size());
    Assert.assertEquals("Apple", products.get(0).getName());

    sort = predicate.getSorted("name", "DESC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Toshiba crx", products.get(0).getName());

    sort = predicate.getSorted("id", "ASC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Toshiba crx", products.get(0).getName());

    sort = predicate.getSorted("id", "DESC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Apple", products.get(0).getName());

    sort = predicate.getSorted("category", "ASC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Toshiba crx", products.get(0).getName());

    sort = predicate.getSorted("category", "DESC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Apple", products.get(0).getName());

    sort = predicate.getSorted("createdDate", "ASC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Toshiba crx", products.get(0).getName());

    sort = predicate.getSorted("createdDate", "DESC");
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertEquals("Apple", products.get(0).getName());

    this.productRepository.deleteAll();
    products = this.productService.getAllProducts(0, 3, sort, List.of(filterResource));
    Assert.assertTrue(products.isEmpty());
  }

  @Test
  public void testQuantityIsEnough() {
    BigDecimal quantityBuy = BigDecimal.valueOf(22);
    Long productId = product1.getId();
    Assert.assertTrue(this.productService.quantityIsEnough(productId, quantityBuy));
  }

  @Test
  public void testQuantityIsEqualValue() {
    BigDecimal quantityBuy = BigDecimal.valueOf(33);
    Long productId = product1.getId();
    Assert.assertTrue(this.productService.quantityIsEnough(productId, quantityBuy));
  }

  @Test(expected = CheckQuantityException.class)
  public void testQuantityIsNotEnoughWithException() {
    BigDecimal quantityBuy = BigDecimal.valueOf(44);
    Product product = this.productRepository.findAll().get(0);
    Assert.assertEquals(this.productService.quantityIsEnough(product.getId(), quantityBuy),
      String.format("Quantity is not enough!!!\nAvailable quantity: %s",
        product.getQuantity()));
  }

  @Test
  public void testQuantityIsNotEnough() {
    BigDecimal quantityBuy = BigDecimal.valueOf(22);
    Product product = this.productRepository.findAll().get(0);
    Assert.assertTrue(this.productService.quantityIsEnough(product.getId(), quantityBuy));
  }

  @Test
  public void testQuantityIsNotEnoughWithEqualsQuantity() {
    BigDecimal quantityBuy = BigDecimal.valueOf(33);
    Product product = this.productRepository.findAll().get(0);
    Assert.assertTrue(this.productService.quantityIsEnough(product.getId(), quantityBuy));
  }

  @Test
  public void testSetQuantity() {
    BigDecimal quantityBuy = BigDecimal.valueOf(30);
    Long productId = this.productRepository.findAll().get(0).getId();
    Assert.assertEquals(this.productService.getById(productId).getQuantity().stripTrailingZeros(),
      BigDecimal.valueOf(33));
    this.productService.setQuantity(quantityBuy, productId);
    Assert.assertEquals(BigDecimal.valueOf(3).stripTrailingZeros(),
      this.productService.getById(productId).getQuantity().stripTrailingZeros());
  }

  @Test
  public void testFindByName() {
    Product
      product =
      this.productRepository.findByName(this.productRepository.findAll().get(0).getName());
    Assert.assertEquals("Toshiba crx", product.getName());
  }

  @Test
  public void testGetAllProductsBySpecifications() {
    FilterResource
      filterResource =
      new FilterResource("name", this.productRepository.findAll().get(0).getName(), "eq");
    Specification specification = this.productService.getAllSpecifications(List.of(filterResource));
    List<Product> products = this.productService.getAllProductsBySpecifications(specification);
    Assert.assertEquals(1, products.size());
    Assert.assertEquals("Toshiba crx", products.get(0).getName());
  }

  @Test
  public void testGetOrderServiceModel() {
    Product product = this.productRepository.findAll().get(0);
    OrderCreateResource
      orderCreateResource =
      this.productService.getOrderResource(product,
        BigDecimal.valueOf(1));
    Assert.assertEquals(orderCreateResource.getProductName(), "Toshiba crx");
    Assert.assertEquals(orderCreateResource.getQuantity(), BigDecimal.ONE);
  }

  @Test(expected = ConflictException.class)
  public void testNamesMatchesWithException() {
    Assert.assertFalse(this.productService.namesMatches("Sony", "Samsung"));
  }

  @Test
  public void testNamesMatches() {
    Assert.assertTrue(this.productService.namesMatches("Sony", "Sony"));
  }

  @Test
  public void testCheckForValidId() {
    Long id = this.productRepository.findAll().get(0).getId();
    Assert.assertTrue(this.productService.checkForValidId(id));
  }

  @Test(expected = NotFoundException.class)
  public void testCheckForValidIdWithException() {
    Long id = 11111L;
    Assert.assertFalse(this.productService.checkForValidId(id));
  }

  @Test
  public void testGetUpdatedProduct() {
    Long id = this.productRepository.findAll().get(0).getId();
    Product product = this.productService.getUpdatedProduct(productCreateResource, id);
    Assert.assertEquals(product.getName(), "Sony");
  }

  @Test
  public void testGetHttpEntity() {
    Product product = this.productRepository.findAll().get(0);
    BigDecimal quantityBuy = BigDecimal.valueOf(33);
    HttpEntity<OrderCreateResource> httpEntity =
      this.productService.getHttpEntity(product, quantityBuy);
    Assert.assertEquals(httpEntity.getBody().getProductName(), "Toshiba crx");
  }

  @Test
  public void testValidationOrder() {
    Long id = this.productRepository.findAll().get(0).getId();
    Assert.assertTrue(this.productService.validationOrder(id, 22L));
  }

  @Test(expected = CheckQuantityException.class)
  public void testValidationOrderWithNegativeQuantity() {
    Long id = this.productRepository.findAll().get(0).getId();
    Assert.assertFalse(this.productService.validationOrder(id, -22L));
  }

  @Test
  public void testValidationProductCreateResource() {
    productCreateResource.setName("");
    BindingResult bindingResult = mock(BindingResult.class);
    when(bindingResult.hasErrors()).thenReturn(false);
    boolean result = this.productService.validationProductCreateResource(bindingResult);
    Assert.assertFalse(bindingResult.hasErrors());
    Assert.assertTrue(result);
  }

  @Test(expected = ValidationException.class)
  public void testValidationProductCreateResourceWithException() {
    BindingResult bindingResult = mock(BindingResult.class);
    when(bindingResult.hasErrors()).thenReturn(true);
    Assert.assertTrue(bindingResult.hasErrors());
    this.productService.validationProductCreateResource(bindingResult);
  }

  @Test(expected = ValidationException.class)
  public void testValidationUpdateProductWithInvalidResource() {
    Long id = this.productRepository.findAll().get(0).getId();
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(true);
    Assert.assertFalse(
      this.productService.validationUpdateProduct(productCreateResource, result, id));
  }

  @Test(expected = NotFoundException.class)
  public void testValidationUpdateProductWithInvalidId() {
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(false);
    Assert.assertFalse(
      this.productService.validationUpdateProduct(productCreateResource, result, 1111L));
  }

  @Test
  public void testValidationUpdateProduct() {
    Long id = this.productRepository.findAll().get(0).getId();
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(false);
    Assert.assertTrue(
      this.productService.validationUpdateProduct(productCreateResource, result, id));
  }

  @Test(expected = ConflictException.class)
  public void testValidationUpdateProductWithExistingName() {
    productCreateResource.setName("Apple");
    Long id = this.productRepository.findAll().get(0).getId();
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(false);
    Assert.assertFalse(
      this.productService.validationUpdateProduct(productCreateResource, result, id));
  }

  @Test
  public void testValidationUpdateProductWithExistingName1() {
    productCreateResource.setName("Toshiba crx");
    Long id = this.productRepository.findAll().get(0).getId();
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(false);
    Assert.assertTrue(
      this.productService.validationUpdateProduct(productCreateResource, result, id));
  }

  @Test
  public void testValidationSortingAndFiltering() {
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(false);
    FilterResource filterResource = new FilterResource("quantity", "0", "ge");
    List<Product> products =
      this.productService.validationSortingAndFiltering("id", "ASC", 0, 2, List.of(filterResource),
        result);
    Assert.assertEquals(2, products.size());
  }
}

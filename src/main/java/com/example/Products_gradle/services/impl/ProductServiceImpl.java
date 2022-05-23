package com.example.Products_gradle.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import com.example.Products_gradle.exeptions.CheckQuantityException;
import com.example.Products_gradle.exeptions.ConflictException;
import com.example.Products_gradle.exeptions.NotFoundException;
import com.example.Products_gradle.exeptions.ValidationException;
import com.example.Products_gradle.model.entities.Product;
import com.example.Products_gradle.web.resource.*;
import com.example.Products_gradle.criteria.ProductPredicate;
import com.example.Products_gradle.criteria.SearchCriteria;
import com.example.Products_gradle.repositories.ProductRepository;
import com.example.Products_gradle.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final RestTemplate restTemplate;
  private final ReentrantLock reentrantLock;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository,
    RestTemplate restTemplate, ReentrantLock reentrantLock) {
    this.productRepository = productRepository;
    this.restTemplate = restTemplate;
    this.reentrantLock = reentrantLock;
  }


  @Override
  public int getAllProductCount(Specification specification) {
    return this.productRepository.findAll(specification).size();
  }

  @Override
  public void setQuantity(BigDecimal quantity, Long id) {
    BigDecimal newQuantity = getById(id).getQuantity().subtract(quantity);
    this.productRepository.setQuantity(newQuantity, id);
  }

  @Override
  public Product seedProduct(Product product) {
    if (product.getCreatedDate() == null) {
      product.setCreatedDate(LocalDate.now());
    }
    if (product.getLastModifiedDate() == null) {
      product.setLastModifiedDate(LocalDate.now());
    }
    try {
      this.productRepository.save(product);
      return product;
    } catch (Exception e) {
      throw new DataIntegrityViolationException(e.getMessage());
    }

  }

  @Override
  public boolean productIsExists(String name) {
    if (this.productRepository.findByName(name) != null) {
      throw new ConflictException("Product with name '" + name + "' is allready exests!!!");
    }
    return true;
  }

  @Override
  public Product getById(Long id) {
    return this.productRepository.findById(id).orElse(null);
  }

  @Override
  public void deleteProductById(Long id) {
    this.productRepository.deleteById(id);
  }

  @Override
  public List<Product> getAllProducts(Integer pageNo, Integer pageSize, Sort sort,
    List<FilterResource> filterResource) {
    Pageable paging = PageRequest.of(pageNo, pageSize, sort);
    Specification specification = getAllSpecifications(filterResource);
    Page<Product> pagedResult = this.productRepository.findAll(specification, paging);
    if (pagedResult.hasContent()) {
      return pagedResult.getContent();
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public List<Product> validationSortingAndFiltering(String orderBy, String direction, Integer page,
    Integer pageSize, List<FilterResource> filterResources,
    BindingResult bindingResult) {
    List<Product> products = new ArrayList<>();
    validationProductCreateResource(bindingResult);
    ProductPredicate predicate = new ProductPredicate();
    Sort sortBy = predicate.getSorted(orderBy, direction);
    products = getAllProducts(page, pageSize, sortBy, filterResources);
    return products;
  }

  @Override
  public List<Product> getAllProductsBySpecifications(Specification specification) {
    return this.productRepository.findAll(specification);
  }

  @Override
  public Specification getAllSpecifications(List<FilterResource> specifications) {
    Specification specification = null;

    for (int i = 0; i < specifications.size(); i++) {
      String field = specifications.get(i).getField();
      String operation = specifications.get(i).getOperation();
      String value = specifications.get(i).getValue();
      SearchCriteria searchCriteria = new SearchCriteria(field, operation, value);
      ProductPredicate predicate = new ProductPredicate(searchCriteria);
      if (specification == null) {
        specification = Specification.where(predicate);
      } else {
        specification = specification.and(predicate);
      }
    }

    return specification;
  }

  @Override
  public OrderCreateResource getOrderResource(Product product,
    BigDecimal quantityBuy) {
    OrderCreateResource order = new OrderCreateResource();
    order.setProductId(product.getId());
    order.setProductName(product.getName());
    order.setQuantity(quantityBuy);
    return order;
  }

  @Override
  public boolean validationProductCreateResource(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      List<String> messages = new ArrayList<>();
      for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
        messages.add(bindingResult.getAllErrors().get(i).getDefaultMessage());
      }
      throw new ValidationException(messages.toString());
    }
    return true;
  }

  @Override
  public boolean namesMatches(String oldName, String newName) {
    if (oldName.equals(newName)) {
      return true;
    }
    throw new ConflictException("This name is already exists!!!");
  }

  @Override
  public boolean checkForValidId(Long id) {
    if (getById(id) == null) {
      throw new NotFoundException("Product with ID:'" + id + "' is no exists!!!");
    }
    return true;
  }

  @Override
  public boolean validationUpdateProduct(ProductCreateResource productCreateResource,
    BindingResult bindingResult, Long id) {
    Product product = new Product();
    if (validationProductCreateResource(bindingResult) && checkForValidId(id)) {
      product = getById(id);
      if (this.productRepository.findByName(
        productCreateResource.getName()) != null && !namesMatches(productCreateResource.getName(),
        product.getName())) {
        throw new ConflictException(
          "Product with name '" + productCreateResource.getName() + "' is allready exests!!!");
      }
    }
    return true;
  }

  @Override
  public boolean validationOrder(Long id, Long quantity) {
    Product product = new Product();
    BigDecimal quantityBuy = BigDecimal.valueOf(quantity);
    if (checkForValidId(id)) {
      product = getById(id);
    }
    if (quantityBuy.compareTo(BigDecimal.ZERO) <= 0) {
      throw new CheckQuantityException("Quantity cannot be a negative number or zero!!!");
    }
    if (quantityIsEnough(id, quantityBuy)) {
      setQuantity(quantityBuy, id);
      this.restTemplate.postForObject("http://eureka-orders/orders",
        getHttpEntity(product, quantityBuy), String.class);
    }
    return true;
  }

  @Override
  public Product getUpdatedProduct(ProductCreateResource productCreateResource, Long id) {
    Product product = new Product();
    product.setId(id);
    product.setName(productCreateResource.getName());
    product.setCategory(productCreateResource.getCategory());
    product.setDescription(productCreateResource.getDescription());
    product.setQuantity(productCreateResource.getQuantity());
    product.setCreatedDate(getById(id).getCreatedDate());
    product.setLastModifiedDate(LocalDate.now());
    return product;
  }

  @Override
  public HttpEntity<OrderCreateResource> getHttpEntity(Product product,
    BigDecimal quantityBuy) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<OrderCreateResource> httpEntity =
      new HttpEntity<>(getOrderResource(product, quantityBuy), headers);
    return httpEntity;
  }

  @Override
  public boolean quantityIsEnough(Long id, BigDecimal quantityBuy) {
    Product product = getById(id);
    BigDecimal productQuantity = product.getQuantity();
    this.reentrantLock.lock();
    try {
      int compare = productQuantity.compareTo(quantityBuy);
      if (compare == 0) {
        return true;
      } else if (compare == 1) {
        return true;
      }
      throw new CheckQuantityException(
        String.format("Quantity is not enough!!!\nAvailable quantity: %s",
          product.getQuantity()));
    } finally {
      this.reentrantLock.unlock();
    }
  }
}

package com.example.Products_gradle.services;

import java.math.BigDecimal;
import java.util.List;
import com.example.Products_gradle.model.entities.Product;
import com.example.Products_gradle.web.resource.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.validation.BindingResult;

public interface ProductService {

  boolean validationOrder(Long id, Long quantity);

  boolean validationUpdateProduct(ProductCreateResource productCreateResource,
    BindingResult bindingResult, Long id);

  boolean namesMatches(String oldName, String newName);

  boolean checkForValidId(Long id);

  boolean validationProductCreateResource(BindingResult bindingResult);

  HttpEntity<OrderCreateResource> getHttpEntity(Product product, BigDecimal quantityBuy);

  Product getUpdatedProduct(ProductCreateResource productCreateResource, Long id);

  List<Product> getAllProductsBySpecifications(Specification specification);

  Specification getAllSpecifications(List<FilterResource> specifications);

  int getAllProductCount(Specification specification);

  void setQuantity(BigDecimal quantity, Long id);

  boolean quantityIsEnough(Long id, BigDecimal quantityBuy);

  Product seedProduct(Product product) throws
    DataIntegrityViolationException;

  boolean productIsExists(String name);

  List<Product> validationSortingAndFiltering(String orderBy, String direction, Integer page,
    Integer pageSize, List<FilterResource> filterResources,
    BindingResult bindingResult);

  List<Product> getAllProducts(Integer pageNo, Integer pageSize, Sort sort,
    List<FilterResource> filterResource);

  Product getById(Long id);

  void deleteProductById(Long id);

  OrderCreateResource getOrderResource(Product product, BigDecimal quantityBuy);

}

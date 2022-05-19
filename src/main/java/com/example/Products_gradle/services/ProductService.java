package com.example.ProductsGradle.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.example.ProductsGradle.model.bindings.FilterBindingModel;
import com.example.ProductsGradle.model.entities.ProductEntity;
import com.example.ProductsGradle.model.serviceModels.OrderCreateResource;
import com.example.ProductsGradle.model.serviceModels.ProductServiceModel;
import com.example.ProductsGradle.model.views.ProductViewModel;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface ProductService {


  List<ProductViewModel> getAllProductsBySpecifications(Specification specification);

  Specification getAllSpecifications(List<FilterBindingModel> specifications);

  ProductViewModel findByName(String name);

  int getAllProductCount();

  void setQuantity(BigDecimal quantity, Long id);

  boolean quantityIsEnough(Long id, BigDecimal quantityBuy);

  ProductEntity seedProduct(ProductServiceModel productServiceModel) throws
    DataIntegrityViolationException;

  boolean productIsExists(String name);

  List<ProductViewModel> getAllProducts(Integer pageNo, Integer pageSize, Sort sort);

  ProductViewModel getById(Long id);

  void deleteProductById(Long id);

  Sort getSorted(String orderBy, String direction);

  OrderCreateResource getOrderResource(ProductViewModel productViewModel, BigDecimal quantityBuy);

  List<ProductViewModel> conversionToListViewModel(List<ProductEntity> products);
}

package com.example.Products_gradle.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import com.example.Products_gradle.model.bindings.FilterBindingModel;
import com.example.Products_gradle.model.bindings.ProductAddBindingModel;
import com.example.Products_gradle.model.entities.ProductEntity;
import com.example.Products_gradle.model.serviceModels.OrderCreateResource;
import com.example.Products_gradle.model.serviceModels.ProductServiceModel;
import com.example.Products_gradle.model.views.ProductRestViewModel;
import com.example.Products_gradle.model.views.ProductViewModel;
import com.example.Products_gradle.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;
  private final ModelMapper mapper;
  private final RestTemplate restTemplate;

  public ProductController(ProductService productService, ModelMapper mapper, RestTemplate restTemplate) {
    this.productService = productService;
    this.mapper = mapper;
    this.restTemplate = restTemplate;
  }


  @PostMapping("add")
  @ApiOperation(value = "Adding a product to the database", response = ProductEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<?> create(@RequestBody @Valid ProductAddBindingModel productAddBindingModel,
    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    ProductServiceModel
      productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);

    if (this.productService.productIsExists(productServiceModel.getName())) {
      return new ResponseEntity<>("This product is already exists!!!", HttpStatus.BAD_REQUEST);
    }
    ProductEntity product = this.productService.seedProduct(productServiceModel);
    ProductViewModel productViewModel = this.mapper.map(product, ProductViewModel.class);
    return new ResponseEntity<>(productViewModel, HttpStatus.CREATED);

  }

  @GetMapping
  @ApiOperation(httpMethod = "GET", value = "Get all products with a specific arrangement", notes = "Page of products sorted by criteria.", response = ProductRestViewModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Products not found"),
    @ApiResponse(code = 500, message = "The products could not be fetched")})
  public ProductRestViewModel allProductsOrderByQuantities(@RequestParam("orderBy") String orderBy, @RequestParam("direction") String direction,
    @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
    List<ProductViewModel> products = new ArrayList<>();
    if (!(orderBy.equals("id") || orderBy.equals("name") || orderBy.equals("category") || orderBy.equals("createdDate"))) {
      orderBy = "name";
    }

    if (!(direction.equals("ASC") || direction.equals("DESC"))) {
      direction = "ASC";
    }
    Sort sortBy = this.productService.getSorted(orderBy, direction);
    double pageCount = Math.ceil((double) this.productService.getAllProductCount() / pageSize);
    if (page >= pageCount && page > 0) {
      page = (int) pageCount - 1;
    } else if (page < 0) {
      page = 0;
    }
    products = this.productService.getAllProducts(page, pageSize, sortBy);
    return new ProductRestViewModel(products, this.productService.getAllProductCount());

  }

  @DeleteMapping(path = "/delete/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "Delete the product by id", response = ProductViewModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Product not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    ProductViewModel productViewModel = this.productService.getById(id);
    if (productViewModel != null) {
      this.productService.deleteProductById(id);
      return new ResponseEntity<>(productViewModel, HttpStatus.OK);
    }
    return new ResponseEntity<>("This product is no exists!!!", HttpStatus.OK);
  }


  @PutMapping("/update/{id}")
  @ApiOperation(httpMethod = "PUT", value = "Update product by ID", notes = "Change the product values", response = ProductViewModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Product not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductAddBindingModel productAddBindingModel,
    BindingResult bindingResult, @PathVariable("id") Long id) {

    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
    }
    ProductViewModel productViewModel = this.productService.getById(id);

    if (productViewModel == null) {
      return new ResponseEntity<>("Invalid ID!!!", HttpStatus.BAD_REQUEST);
    } else {
      ProductServiceModel productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);
      boolean namesMatches = productViewModel.getName().equals(productServiceModel.getName());

      if (this.productService.productIsExists(productServiceModel.getName()) && !namesMatches) {
        return new ResponseEntity<>("This name is already exists!!!", HttpStatus.BAD_REQUEST);
      } else {
        productServiceModel.setId(id);
        this.productService.seedProduct(productServiceModel);
        return new ResponseEntity<>(this.productService.getById(id), HttpStatus.OK);
      }
    }
  }

  @PostMapping("/{id}/order/{quantity}")
  @ApiOperation(httpMethod = "POST", value = "Order a product by ID and quantity", response = ProductViewModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Products not found"),
    @ApiResponse(code = 500, message = "The products could not be fetched")})
  public synchronized ResponseEntity<?> buyProduct(@PathVariable("id") Long id, @PathVariable("quantity") Long quantity) {

    ProductViewModel productViewModel = this.productService.getById(id);

    if (productViewModel == null) {
      return new ResponseEntity<>("Invalid ID!!!", HttpStatus.BAD_REQUEST);
    }

    BigDecimal quantityBuy = BigDecimal.valueOf(quantity);
    if (this.productService.quantityIsEnough(id, quantityBuy)) {
      this.productService.setQuantity(quantityBuy, id);
      String uri = "http://eureka-orders/orders";
      OrderCreateResource
        orderCreateResource = this.productService.getOrderResource(productViewModel, quantityBuy);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<OrderCreateResource> httpEntity = new HttpEntity<>(orderCreateResource, headers);
      this.restTemplate.postForObject(uri, httpEntity, String.class);
      return new ResponseEntity<>(this.productService.getById(id), HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(String.format("Quantity is not enough!!!\nAvailable quantity: %s", productViewModel.getQuantity()), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/filter")
  @ApiOperation(httpMethod = "GET", value = "Products  filtering", notes = "Filtering products by name, category, quantity and description fields", response = ProductRestViewModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Products not found"),
    @ApiResponse(code = 500, message = "The products could not be fetched")})
  public ResponseEntity<?> filter(@RequestBody @Valid List<FilterBindingModel> filterBindingModels, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    ProductRestViewModel productRestViewModel = new ProductRestViewModel();
    Specification specification = this.productService.getAllSpecifications(filterBindingModels);
    List<ProductViewModel> products = this.productService.getAllProductsBySpecifications(specification);
    productRestViewModel.setProducts(products);
    productRestViewModel.setTotalRecords(products.size());
    return new ResponseEntity<>(productRestViewModel, HttpStatus.OK);
  }
}

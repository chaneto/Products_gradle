package com.example.Products_gradle.web;

import java.util.List;
import javax.validation.Valid;
import com.example.Products_gradle.model.entities.Product;
import com.example.Products_gradle.services.ProductService;
import com.example.Products_gradle.web.assembler.ProductAssembler;
import com.example.Products_gradle.web.resource.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;
  private final ProductAssembler productAssembler;

  @Autowired
  public ProductController(ProductService productService, ProductAssembler productAssembler) {
    this.productService = productService;
    this.productAssembler = productAssembler;
  }


  @PostMapping("/add")
  @ApiOperation(value = "Adding a product to the database", response = Product.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<?> create(@RequestBody @Valid ProductCreateResource productCreateResource, BindingResult bindingResult) {
    Product product = new Product();
    if (this.productService.validationProductCreateResource(bindingResult) && this.productService.productIsExists(productCreateResource.getName())) {
      product = this.productService.seedProduct(this.productAssembler.assembleProduct(productCreateResource));
    }
    return new ResponseEntity<>(this.productAssembler.assembleProductResource(product), HttpStatus.CREATED);
  }

  @DeleteMapping(path = "/delete/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "Delete the product by id", response = ProductResource.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Product not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    Product product = new Product();
    if (this.productService.checkForValidId(id)) {
      product = this.productService.getById(id);
      this.productService.deleteProductById(id);
    }
    return new ResponseEntity<>(this.productAssembler.assembleProductResource(product), HttpStatus.OK);
  }


  @PutMapping("/update/{id}")
  @ApiOperation(httpMethod = "PUT", value = "Update product by ID", notes = "Change the product values", response = ProductResource.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Product not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductCreateResource productCreateResource, BindingResult bindingResult, @PathVariable("id") Long id) {
    Product product = new Product();
    if (this.productService.validationUpdateProduct(productCreateResource, bindingResult, id)) {
      product = this.productService.seedProduct(
        this.productService.getUpdatedProduct(productCreateResource, id));
    }
    return new ResponseEntity<>(this.productAssembler.assembleProductResource(product),
      HttpStatus.OK);
  }

  @PostMapping("/{id}/order/{quantity}")
  @ApiOperation(httpMethod = "POST", value = "Order a product by ID and quantity", response = ProductResource.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Products not found"),
    @ApiResponse(code = 500, message = "The products could not be fetched")})
  public synchronized ResponseEntity<?> buyProduct(@PathVariable("id") Long id,
    @PathVariable("quantity") Long quantity) {
    this.productService.validationOrder(id, quantity);
    return new ResponseEntity<>(this.productAssembler.assembleProductResource(this.productService.getById(id)), HttpStatus.BAD_REQUEST);

  }

  @GetMapping
  @ApiOperation(httpMethod = "GET", value = "Get all products with a specific arrangement", notes = "Page of products sorted by criteria.", response = ProductRestResource.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Products not found"),
    @ApiResponse(code = 500, message = "The products could not be fetched")})
  public ResponseEntity<?> allProductsOrderByQuantities(@RequestParam("orderBy") String orderBy,
    @RequestParam("direction") String direction,
    @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,
    @RequestBody @Valid List<FilterResource> filterResources,
    BindingResult bindingResult) {
    List<Product> products = this.productService.validationSortingAndFiltering(orderBy, direction, page, pageSize, filterResources, bindingResult);
    return new ResponseEntity<>(new ProductRestResource(this.productAssembler.assembleProductsResource(products), this.productService.getAllProductCount(this.productService.getAllSpecifications(filterResources))), HttpStatus.OK);
  }
}

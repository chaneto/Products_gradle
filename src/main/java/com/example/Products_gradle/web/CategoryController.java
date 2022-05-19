package com.example.Products_gradle.web;

import java.util.List;

import com.example.Products_gradle.model.views.CategoryViewModel;
import com.example.Products_gradle.services.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  @ApiOperation(httpMethod = "GET", value = "Get all category", notes = "Information on existing categories and all products available in them", response = CategoryViewModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "Not found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
  public ResponseEntity<List<CategoryViewModel>> getAllCategory() {
    return new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
  }
}

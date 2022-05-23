package com.example.Products_gradle.services;

import java.util.List;
import com.example.Products_gradle.web.resource.CategoryResource;

public interface CategoryService {

  List<CategoryResource> getAllCategory();
}

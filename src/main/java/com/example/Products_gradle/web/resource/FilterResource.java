package com.example.Products_gradle.web.resource;

import java.util.*;
import javax.validation.Valid;

import com.example.Products_gradle.criteria.SearchCriteria;

public class FilterResource {

  @Valid
  private List<SearchCriteria> list;

  public FilterResource() {
    this.list = new ArrayList<>();
  }

  public List<SearchCriteria> getList() {
    return list;
  }

  public void setList(List<SearchCriteria> list) {
    this.list = list;
  }
}

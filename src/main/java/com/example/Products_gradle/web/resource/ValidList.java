package com.example.Products_gradle.web.resource;

import java.util.*;
import javax.validation.Valid;

public class ValidList {

  @Valid
  private List<FilterResource> list;

  public ValidList() {
    this.list = new ArrayList<>();
  }

  public List<FilterResource> getList() {
    return list;
  }

  public void setList(List<FilterResource> list) {
    this.list = list;
  }
}

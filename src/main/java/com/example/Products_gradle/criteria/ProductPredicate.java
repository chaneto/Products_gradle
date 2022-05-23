package com.example.Products_gradle.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.example.Products_gradle.model.entities.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ProductPredicate implements Specification<Product> {

  private SearchCriteria criteria;
  public ProductPredicate() {
  }

  public ProductPredicate(SearchCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate
    (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

    String field = this.criteria.getField();
    String value = this.criteria.getValue().toString();
    String operation = this.criteria.getOperation();
    boolean isNumeric = this.criteria.getValue().toString().chars().allMatch(Character::isDigit);

    if (field.equals("name") || field.equals("quantity") || field.equals(
      "description") || field.equals("category")) {
      if (field.equals("quantity")) {
        if (operation.equals("ge") && isNumeric) {
          return builder.greaterThan(root.get(field), value);
        } else if (operation.equals("le") && isNumeric) {
          return builder.lessThan(root.get(field), value);
        } else if (operation.equals("eq") && isNumeric) {
          return builder.equal(root.get(field), value);
        }
      } else {
        if (operation.equals("contains")) {
          return builder.like(root.get(field), "%" + value + "%");
        } else if (this.criteria.getOperation().equals("bg")) {
          return builder.like(root.get(field), value + "%");
        } else if (operation.equals("eq")) {
          return builder.equal(root.get(field), value);
        }
      }
    }
    return null;
  }

  public Sort getSorted(String orderBy, String direction) {
    Sort sortBy = null;
    if (orderBy.equals("id") && direction.equals("ASC")) {
      sortBy = Sort.by("id");
    } else if (orderBy.equals("id") && direction.equals("DESC")) {
      sortBy = Sort.by("id").descending();
    } else if (orderBy.equals("name") && direction.equals("ASC")) {
      sortBy = Sort.by("name");
    } else if (orderBy.equals("name") && direction.equals("DESC")) {
      sortBy = Sort.by("name").descending();
    } else if (orderBy.equals("category") && direction.equals("ASC")) {
      sortBy = Sort.by("category");
    } else if (orderBy.equals("category") && direction.equals("DESC")) {
      sortBy = Sort.by("category").descending();
    } else if (orderBy.equals("createdDate") && direction.equals("ASC")) {
      sortBy = Sort.by("createdDate");
    } else if (orderBy.equals("createdDate") && direction.equals("DESC")) {
      sortBy = Sort.by("createdDate").descending();
    }
    return sortBy;
  }

  public SearchCriteria getCriteria() {
    return criteria;
  }

  public void setCriteria(SearchCriteria criteria) {
    this.criteria = criteria;
  }
}

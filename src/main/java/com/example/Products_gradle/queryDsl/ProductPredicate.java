package com.example.Products_gradle.queryDsl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.example.Products_gradle.model.entities.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductPredicate implements Specification<ProductEntity> {


  private SearchCriteria criteria;

  public ProductPredicate(SearchCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate
    (Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

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
}

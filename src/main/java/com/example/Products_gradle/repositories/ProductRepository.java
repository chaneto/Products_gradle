package com.example.Products_gradle.repositories;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import com.example.Products_gradle.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
  JpaSpecificationExecutor<Product> {

  Product findByName(String name);

  @Query("select p.category, count(p.id) from Product as p group by p.category order by count(p.id)")
  List<String[]> getAllCategory();

  void deleteById(Long id);

  @Modifying
  @Transactional
  @Query("update Product as p set p.quantity = :quantity where p.id = :id")
  void setQuantity(@Param("quantity") BigDecimal quantity, @Param("id") Long id);

  Page<Product> findAll(Pageable pageable);
}

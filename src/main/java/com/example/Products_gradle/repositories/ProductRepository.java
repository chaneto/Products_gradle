package com.example.Products_gradle.repositories;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import com.example.Products_gradle.model.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>,
  JpaSpecificationExecutor<ProductEntity> {

  ProductEntity findByName(String name);

  @Query("select p.category, count(p.id) from ProductEntity as p group by p.category order by count(p.id)")
  List<String[]> getAllCategory();

  void deleteById(Long id);

  @Modifying
  @Transactional
  @Query("update ProductEntity as p set p.quantity = :quantity where p.id = :id")
  void setQuantity(@Param("quantity") BigDecimal quantity, @Param("id") Long id);

  Page<ProductEntity> findAll(Pageable pageable);
}

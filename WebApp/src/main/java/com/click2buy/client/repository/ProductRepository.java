package com.click2buy.client.repository;

import com.click2buy.client.model.Category;
import com.click2buy.client.model.Maker;
import com.click2buy.client.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer>,
  JpaSpecificationExecutor<Product> {

  List<Product> findByCreationDateTimeAfterOrderByCreationDateTimeAsc(LocalDateTime after);

  Page<Product> findByCategoryName(String name, Pageable pageRequest);

  Page<Product> findByCategoryName(String name, Pageable pageRequest,
    Specification<Product> specification);

  @Query("SELECT DISTINCT p.maker.name from Product p WHERE p.category.name = :categoryName")
  List<String> getMakersOfProductsByCategory(@Param("categoryName") String category);

  @Query("SELECT Max(p.price) from Product p WHERE p.category.name = :categoryName")
  int getMaxPriceByCategoryName(@Param("categoryName") String categoryName);

  @Query("SELECT Min(p.price) from Product p WHERE p.category.name = :categoryName")
  int getMinProductPriceByCategoryName(@Param("categoryName") String categoryName);

  Optional<Product> findProductById(Integer id);

  List<Product> findProductByIdIn(Set<Integer> ids);
}

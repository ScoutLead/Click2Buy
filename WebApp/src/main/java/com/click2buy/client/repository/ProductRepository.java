package com.click2buy.client.repository;

import com.click2buy.client.model.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCreationDateTimeAfterOrderByCreationDateTimeAsc(LocalDateTime after);
    Page<Product> findByCategoryName(String name, Pageable pageRequest);
}

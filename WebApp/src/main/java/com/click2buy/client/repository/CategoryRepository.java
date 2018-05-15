package com.click2buy.client.repository;


import com.click2buy.client.model.Category;
import com.click2buy.client.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  List<Category> findByParentIsNull();
}

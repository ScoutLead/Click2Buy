package com.click2buy.client.service.implementation;

import com.click2buy.client.model.Category;
import com.click2buy.client.repository.CategoryRepository;
import com.click2buy.client.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryServiceImpl(
    @Qualifier("categoryRepository")
      CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Category> getRootCategoriesWithChildren() {
    return categoryRepository.findByParentIsNull();
  }
}

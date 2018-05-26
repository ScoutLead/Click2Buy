package com.click2buy.client.service;

import com.click2buy.client.model.Category;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
  List<Category> getRootCategoriesWithChildren();

  Optional<Category> getCategoryByName(String name);

  static List<Category> getAncestors(Category category) {
    return ancestors(category, new ArrayList<>());
  }

  static List<Category> ancestors(Category category, List<Category> acc) {
    acc.add(0, category);
    if(category.getParent() == null) return acc;
    return ancestors(category.getParent(), acc);
  }
}

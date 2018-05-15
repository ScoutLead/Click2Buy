package com.click2buy.client.service;

import com.click2buy.client.model.Category;
import java.util.List;

public interface CategoryService {
  List<Category> getRootCategoriesWithChildren();
}

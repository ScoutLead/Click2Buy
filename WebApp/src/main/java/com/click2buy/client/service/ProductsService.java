package com.click2buy.client.service;

import com.click2buy.client.model.Product;
import java.util.List;
import java.util.Map;

public interface ProductsService {
  Map<String, List<Product>> getCategoriesNameAndNewestProducts();
}

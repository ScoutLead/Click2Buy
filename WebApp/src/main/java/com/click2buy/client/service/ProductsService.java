package com.click2buy.client.service;

import com.click2buy.client.dto.Sorting;
import com.click2buy.client.model.Product;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface ProductsService {
  Map<String, List<Product>> getCategoriesNameAndNewestProducts();
  Page<Product> getProductsByCategory(String category, int page,
    Sorting sortingParam, JsonNode filterQuery);

  Page<Product> getProductsByCategory(String category, int page,
    Sorting sortingParam);

  Integer getMinPriceByCategory(String category);

  List<String> getMakersByProductCategory(String category);

  Integer getMaxPriceByCategory(String category);
}

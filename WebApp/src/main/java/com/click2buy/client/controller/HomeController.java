package com.click2buy.client.controller;

import com.click2buy.client.service.CategoryService;
import com.click2buy.client.service.ProductsService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final CategoryService categoryService;
  private final ProductsService productsService;

  public HomeController(CategoryService categoryService, ProductsService productsService) {
    this.categoryService = categoryService;
    this.productsService = productsService;
  }

  @GetMapping("/")
  public String home(Map<String, Object> model) {
    model.put("message", "hello");
    model.put("categories", categoryService.getRootCategoriesWithChildren());
    model.put("newest", productsService.getCategoriesNameAndNewestProducts());
    return "home";
  }
}

package com.click2buy.client.controller;

import com.click2buy.client.service.CategoryService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final CategoryService categoryService;

  public HomeController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/")
  public String home(Map<String, Object> model) {
    model.put("message", "hello");
    model.put("categories", categoryService.getRootCategoriesWithChildren());
    return "home";
  }
}

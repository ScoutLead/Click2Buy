package com.click2buy.client.controller;


import static com.click2buy.client.service.CategoryService.getAncestors;
import static java.util.stream.Collectors.toList;

import com.click2buy.client.dto.Sorting;
import com.click2buy.client.model.Category;
import com.click2buy.client.model.Product;
import com.click2buy.client.service.CategoryService;
import com.click2buy.client.service.ProductsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/goods")
public class GoodsController {

  public static class Link {
    public String url;
    public String name;

    public Link(String url, String name) {
      this.url = url;
      this.name = name;
    }
  }

  private final CategoryService categoryService;
  private final ProductsService productsService;
  private final MessageSourceAccessor messages;

  public GoodsController(
    CategoryService categoryService,
    ProductsService productsService,
    MessageSource messageSource
    ) {
    this.categoryService = categoryService;
    this.productsService = productsService;
    this.messages = new MessageSourceAccessor(messageSource);
  }

  @GetMapping("/{category}")
  public String showGoodsBy(
    @PathVariable(value = "category") String category,
    @RequestParam(value = "page", defaultValue="1") int page,
    @RequestParam(value = "sort_by", defaultValue="popular") String sortingParam,
    Map<String, Object> model) {
    Map<String, Link> sorting = sorting(category);
    List<Category> rootCategoriesWithChildren = categoryService.getRootCategoriesWithChildren();
    model.put("main_category", category);
    model.put("categories", rootCategoriesWithChildren);
    Page<Product> productPage = productsService
      .getProductsByCategory(category, page - 1, Sorting.valueOf(sortingParam));
    model.put("goodsByCategory", groupByThree(productPage.getContent()));
    model.put("current", productPage.getNumber() + 1);
    model.put("last", productPage.getTotalPages());
    model.put("sortingParamName", sorting.get(sortingParam).name);
    model.put("sortingParam", sortingParam);
    model.put("sortingLinks", sorting.values());
    model.put("first", 1);
    model.put("pages", pageIndexes(productPage.getTotalPages(), productPage.getNumber() + 1));
    categoryService.getCategoryByName(category)
      .ifPresent(x -> {
        model.put("main_categories", getAncestors(x));
      });

    return "goods";
  }

  private Map<String, Link> sorting(String category) {
    String mainUrl = "~/goods/" + category + "?sort_by=";
    Map<String, Link> sortBy = new HashMap<>();
    sortBy.put(Sorting.news.toString(), new Link(mainUrl + "news",
      messages.getMessage("sorting.byNew")));
    sortBy.put(Sorting.popular.toString(), new Link(mainUrl + "popular",
      messages.getMessage("sorting.byPopular")));
    sortBy.put(Sorting.rating.toString(), new Link(mainUrl + "rating",
      messages.getMessage("sorting.byRating")));
    sortBy.put(Sorting.priceAsc.toString(), new Link(mainUrl + "priceAsc",
      messages.getMessage("sorting.fromCheapToExp")));
    sortBy.put(Sorting.priceDesc.toString(), new Link(mainUrl + "priceDesc",
      messages.getMessage("sorting.fromExpToCheap")));
    return sortBy;
  }

  private List<Integer> pageIndexes(int totalPages, int currentPage) {
    if(totalPages <=3 && currentPage <= totalPages) return generateIndexes(1, totalPages);
    if(currentPage >= totalPages - 1) return generateIndexes(totalPages - 2, totalPages);
    if(currentPage == 1) return generateIndexes(currentPage, currentPage + 2);
    return generateIndexes(currentPage - 1, currentPage + 1);
  }

  private List<Integer> generateIndexes(int first, int last) {
    return IntStream.rangeClosed(first, last).boxed().collect(toList());
  }

  private List<List<Product>> groupByThree(List<Product> products) {
    List<List<Product>> arr = new ArrayList<>();
    for(int i = 0; i < products.size(); i++) {
      try {
        arr.get(i / 3).add(i % 3, products.get(i));
      } catch (Exception e) {
        List<Product> p = new ArrayList<>();
        p.add(i % 3, products.get(i));
        arr.add(i / 3, p);
      }
    }
    return arr;
  }

}

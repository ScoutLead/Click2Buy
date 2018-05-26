package com.click2buy.client.controller;


import static com.click2buy.client.service.CategoryService.getAncestors;
import static java.util.stream.Collectors.toList;

import com.click2buy.client.dto.Sorting;
import com.click2buy.client.model.Category;
import com.click2buy.client.model.Product;
import com.click2buy.client.service.CategoryService;
import com.click2buy.client.service.ProductsService;
import com.click2buy.client.utils.FilterUrlBuilder;
import com.click2buy.client.utils.Range;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
@RequestMapping(value = "/goods")
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
    @RequestParam(value = "page", defaultValue = "1") int page,
    @RequestParam(value = "sort_by", defaultValue = "popular") String sortingParam,
    @RequestParam(value = "filterBy", required = false) String filterBy,
    Map<String, Object> model) throws IOException {
    Map<String, Link> sorting = sorting(category);
    List<Category> rootCategoriesWithChildren = categoryService.getRootCategoriesWithChildren();
    model.put("filterBy", filterBy);
    model.put("filterBuilder", new FilterUrlBuilder(filterBy));
    model.put("sortBy", sortingParam);
    model.put("makers", convertToChosen(
      productsService.getMakersByProductCategory(category),
      chosenMakers(filterBy)));
    model.put("main_category", category);
    model.put("categories", rootCategoriesWithChildren);
    Page<Product> productPage = filterBy == null ?
      productsService
        .getProductsByCategory(category, page - 1, Sorting.valueOf(sortingParam)) :
      productsService
        .getProductsByCategory(category, page - 1,
          Sorting.valueOf(sortingParam),
          new ObjectMapper().readTree(filterBy));
    model.put("goodsByCategory", groupByThree(productPage.getContent()));
    model.put("current", productPage.getNumber() + 1);
    model.put("last", productPage.getTotalPages());
    model.put("sortingParamName", sorting.get(sortingParam).name);
    model.put("sortingParam", sortingParam);
    model.put("sortingLinks", sorting.values());
    model.put("minPrice", productsService.getMinPriceByCategory(category));
    model.put("maxPrice", productsService.getMaxPriceByCategory(category));
    getPrice(filterBy)
      .map(r -> {
        model.put("curMinPrice", r.min);
        model.put("curMaxPrice", r.max);
        return 1;
      }).orElseGet(() -> {
        model.put("curMinPrice", model.get("minPrice"));
        model.put("curMaxPrice", model.get("maxPrice"));
        return null;
    });

    model.put("filterByTags", chosenMakers(filterBy));

    model.put("first", 1);
    model.put("pages", pageIndexes(productPage.getTotalPages(), productPage.getNumber() + 1));
    categoryService.getCategoryByName(category)
      .ifPresent(x -> model.put("main_categories", getAncestors(x)));

    return "goods";
  }

  private Optional<Range> getPrice(String filterBy) {
    try {
      Range range = new Range();
      String price = "price";
      JsonNode jsonNode = new ObjectMapper().readTree(filterBy);
      range.min = jsonNode.findParents(price).get(0).get(price).get("$gte").asInt();
      range.max = jsonNode.findParents(price).get(1).get(price).get("$lte").asInt();
      return Optional.of(range);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private Map<String, Boolean> convertToChosen(List<String> allString, List<String> chosen) {
    return allString.stream().collect(Collectors.toMap(x -> x,
      x -> chosen.stream().anyMatch(s -> s.equals(x))));
  }


  private List<String> chosenMakers(String filterBy) {
    try {
      String price = "maker";
      JsonNode jsonNode = new ObjectMapper().readTree(filterBy);
      JsonNode jsonNode1 = jsonNode.findParents(price).get(0);
      Iterator<JsonNode> in;
      if(jsonNode1 != null) {
        in = jsonNode1.get(price).get("$in").iterator();
      } else {
        in = jsonNode.findParents(price).get(0).get("$in").iterator();
      }
      List<String> makers = new ArrayList<>();
      while(in.hasNext()) {
        makers.add(in.next().asText());
      }
      return makers;
    } catch (Exception e) {
      return Collections.emptyList();
    }
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
    if (totalPages <= 3 && currentPage <= totalPages) {
      return generateIndexes(1, totalPages);
    }
    if (currentPage >= totalPages - 1) {
      return generateIndexes(totalPages - 2, totalPages);
    }
    if (currentPage == 1) {
      return generateIndexes(currentPage, currentPage + 2);
    }
    return generateIndexes(currentPage - 1, currentPage + 1);
  }

  private List<Integer> generateIndexes(int first, int last) {
    return IntStream.rangeClosed(first, last).boxed().collect(toList());
  }

  private List<List<Product>> groupByThree(List<Product> products) {
    List<List<Product>> arr = new ArrayList<>();
    for (int i = 0; i < products.size(); i++) {
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

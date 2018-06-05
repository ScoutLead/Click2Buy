package com.click2buy.client.controller;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.click2buy.client.model.Product;
import com.click2buy.client.service.ProductsService;
import com.click2buy.client.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonalInformationController {

  private final ProductsService productsService;

  public PersonalInformationController(ProductsService productsService) {
    this.productsService = productsService;
  }
  @GetMapping("/bucket")
  public String bucket(@CookieValue(value = "bucket", required = false) String bucket,
    @RequestParam(value = "page", defaultValue = "1") int page,
    Map<String, Object> model) throws JsonProcessingException {
    Set<Integer> ids = Arrays.stream(bucket.split("-")).map(Integer::valueOf)
      .collect(toSet());
    List<Product> productsByIds = productsService.findProductsByIds(ids);
    model.put("goodsInBucket", productsByIds);
    String value = new ObjectMapper().writeValueAsString(
      productsByIds.stream().collect(toMap(Product::getId, Product::getPrice)));
    model.put("goodsInBucketJson", value);
    return "bucket";
  }

  @GetMapping("/successOrder.html")
  public String successOrder(HttpServletResponse response) {
    response.addCookie(new Cookie("bucket", ""));
    return "successOrder";
  }
}

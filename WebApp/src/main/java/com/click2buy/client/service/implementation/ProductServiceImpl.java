package com.click2buy.client.service.implementation;

import com.click2buy.client.model.Category;
import com.click2buy.client.model.Product;
import com.click2buy.client.repository.ImageRepository;
import com.click2buy.client.repository.ProductRepository;
import com.click2buy.client.service.ProductsService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements ProductsService {

  private final ProductRepository productRepository;

  private final ImageRepository imageRepository;

  @Autowired
  public ProductServiceImpl(
    @Qualifier("productRepository")
      ProductRepository productRepository,
    @Qualifier("imageRepository")
      ImageRepository imageRepository) {
    this.productRepository = productRepository;
    this.imageRepository = imageRepository;
  }

  @Override
  public Map<String, List<Product>> getCategoriesNameAndNewestProducts() {
    Map<String, List<Product>> listMap = productRepository
      .findByCreationDateTimeAfterOrderByCreationDateTimeAsc(
        LocalDateTime.now().minusWeeks(1)).stream()
      .collect(Collectors.groupingBy(x -> getAncestorName(x.getCategory()),
        limitingList(4)));
    listMap.forEach((k, v) ->
      v.forEach(product ->
        Optional
          .ofNullable(imageRepository.findByMainAndProductId(true, product.getId()))
          .ifPresent(product::setHeadImage)
      ));
    return listMap;
  }

  @Override
  public Page<Product> getProductsByCategory(String category, int page) {
    return productRepository
      .findByCategoryName(category, new PageRequest(page, 3))
      .map(this::addHeadImage);
  }

  private Product addHeadImage(Product product) {
    Optional
      .ofNullable(imageRepository.findByMainAndProductId(true, product.getId()))
      .ifPresent(product::setHeadImage);
    return product;
  }

  private static <T> Collector<T, ?, List<T>> limitingList(int limit) {
    return Collector.of(
      ArrayList::new,
      (l, e) -> {
        if (l.size() < limit) {
          l.add(e);
        }
      },
      (l1, l2) -> {
        l1.addAll(l2.subList(0, Math.min(l2.size(), Math.max(0, limit - l1.size()))));
        return l1;
      }
    );
  }

  private String getAncestorName(Category category) {
    if (category.getParent() == null) {
      return category.getName();
    } else {
      return getAncestorName(category.getParent());
    }
  }

}

package com.click2buy.client.service.implementation;

import static org.springframework.data.jpa.domain.Specifications.where;

import com.click2buy.client.dto.Sorting;
import com.click2buy.client.model.Category;
import com.click2buy.client.model.Product;
import com.click2buy.client.repository.ImageRepository;
import com.click2buy.client.repository.ProductRepository;
import com.click2buy.client.service.ProductsService;
import com.click2buy.client.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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
  public Page<Product> getProductsByCategory(String category, int page,
    Sorting sortingParam, JsonNode filterQuery) {
    String[] entityAndDirection = sortingParam.entityName().split("\\.");
    String entity = entityAndDirection[0];
    Direction direction = entityAndDirection.length == 1 ?
      Direction.DESC : Direction.fromString(entityAndDirection[1]);
    return productRepository
      .findAll(where(categoryEquals(category)).and(toSpecification(filterQuery)),
        new PageRequest(page, 21, direction, entity))
      .map(this::addHeadImage);
  }

  private Specification<Product> categoryEquals(String category) {
    return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      return cb.equal(root.join("category").get("name"), category);
    };
  }

  private Specification<Product> toSpecification(JsonNode filterQuery) {
    return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (filterQuery.get("$and") != null) {
        return cb.and(predicates(filterQuery.get("$and"), root, cb));
      } else if (filterQuery.fields().hasNext()) {
        return cb.and(predicates(filterQuery, root, cb));
      }
      return cb.conjunction();
    };
  }

  private Predicate[] predicates(JsonNode filterQuery, Root<Product> root, CriteriaBuilder cb) {
    return IntStream.range(0, filterQuery.size())
      .mapToObj(i -> {
        JsonNode nodeEntry = filterQuery.get(i) == null ? filterQuery : filterQuery.get(i);
        Entry<String, JsonNode> next = nodeEntry.fields().next();
        if(next.getValue().isArray()) {
          return comparison(next.getKey(), getFieldName(root, next.getKey()), next.getValue())
            .apply(cb);
        } else {
          Entry<String, JsonNode> next1 = next.getValue().fields().next();
          return comparison(next1.getKey(), getFieldName(root, next.getKey()), next1.getValue())
            .apply(cb);
        }


      }).toArray(Predicate[]::new);
  }

  private Path<Object> getFieldName(Root<Product> root, String key) {
    if (key.equals("maker")) {
      return root.get(key).get("name");
    }
    return root.get(key);
  }


  private Function<CriteriaBuilder, Predicate> comparison(String name, Path fieldName,
    JsonNode fieldValue) {
    switch (name) {
      case "$in":
        return cb -> fieldName.in(Utils.convertToArray(fieldValue));
      case "$gte":
        return cb -> cb.ge(fieldName, fieldValue.asInt());
      case "$lte":
      default:
        return cb -> cb.le(fieldName, fieldValue.asInt());
    }
  }


  @Override
  public Page<Product> getProductsByCategory(String category, int page,
    Sorting sortingParam) {
    String[] entityAndDirection = sortingParam.entityName().split("\\.");
    String entity = entityAndDirection[0];
    Direction direction = entityAndDirection.length == 1 ?
      Direction.DESC : Direction.fromString(entityAndDirection[1]);
    return productRepository
      .findByCategoryName(category, new PageRequest(page, 21, direction, entity))
      .map(this::addHeadImage);
  }

  @Override
  public Integer getMinPriceByCategory(String category) {
    return productRepository.getMinProductPriceByCategoryName(category);
  }

  @Override
  public List<String> getMakersByProductCategory(String category) {
    return productRepository.getMakersOfProductsByCategory(category);
  }

  @Override
  public Integer getMaxPriceByCategory(String category) {
    return productRepository.getMaxPriceByCategoryName(category);
  }

  @Override
  public Optional<Product> getProductById(Integer id) {
    return productRepository.findProductById(id);
  }

  @Override
  public List<Product> findProductsByIds(Set<Integer> ids) {
    List<Product> productByIdIn = productRepository.findProductByIdIn(
      ids);
    productByIdIn.forEach(product ->
      Optional
        .ofNullable(imageRepository.findByMainAndProductId(true, product.getId()))
        .ifPresent(product::setHeadImage)
    );
    return productByIdIn;
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

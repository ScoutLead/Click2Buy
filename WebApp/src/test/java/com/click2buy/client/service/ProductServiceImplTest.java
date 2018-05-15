package com.click2buy.client.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.click2buy.client.model.Category;
import com.click2buy.client.model.Product;
import com.click2buy.client.repository.ProductRepository;
import com.click2buy.client.service.implementation.ProductServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProductServiceImplTest {

  private ProductRepository productRepository;

  private ProductsService productService;

  @Before
  public void setUp() {
    productRepository = Mockito.mock(ProductRepository.class);
    productService = new ProductServiceImpl(productRepository);
  }
  @Test
  public void getCategoriesNameAndNewestProductsTest() {
    Category category = new Category("p");
    Category category1 = new Category("a", category);
    Category category2 = new Category("c", category);
    Category category3 = new Category("b");
    Category category4 = new Category("d", category3);
    when(productRepository.findByCreationDateTimeAfterOrderByCreationDateTimeAsc(any()))
      .thenReturn(Arrays.asList(getProduct(category),
        getProduct(category1),
        getProduct(category2),
        getProduct(new Category("w", category)),
        getProduct(new Category("w1", category)),
        getProduct(category4),
        getProduct(new Category("w", category3)),
        getProduct(new Category("w", category3)),
        getProduct(new Category("l")),
        getProduct(category3)));

    Map<String, List<Product>> newestProducts = productService
      .getCategoriesNameAndNewestProducts();

    assertEquals(3, newestProducts.entrySet().size());
    assertTrue(newestProducts.containsKey("p"));
    assertTrue(newestProducts.containsKey("b"));
    assertTrue(newestProducts.containsKey("l"));
    assertEquals(4, newestProducts.get("p").size());
    assertEquals(4, newestProducts.get("b").size());
    assertEquals(1, newestProducts.get("l").size());
  }

  private Product getProduct(Category category) {
    Product product = new Product();
    product.setCategory(category);
    return product;
  }

}
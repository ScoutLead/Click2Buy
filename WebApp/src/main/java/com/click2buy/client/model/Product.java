package com.click2buy.client.model;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "creation_date")
  private LocalDateTime creationDateTime;

  @Column(name = "name")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "maker")
  private Maker maker;

  @Column(name = "rating")
  private double rating;

  @Column(name = "count")
  private int count;

  @Column(name = "count_of_selled")
  private int sellingCount;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "currency")
  private String currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private List<GoodsImage> images;

  @Transient
  private GoodsImage headImage;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(LocalDateTime creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Maker getMaker() {
    return maker;
  }

  public void setMaker(Maker maker) {
    this.maker = maker;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getSellingCount() {
    return sellingCount;
  }

  public void setSellingCount(int sellingCount) {
    this.sellingCount = sellingCount;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public GoodsImage getHeadImage() {
    return headImage;
  }

  public void setHeadImage(GoodsImage headImage) {
    this.headImage = headImage;
  }

  public List<GoodsImage> getImages() {
    return images;
  }

  public void setImages(List<GoodsImage> images) {
    this.images = images;
  }

  public List<Boolean> ratingInStars() {
    int intRating = (int)Math.round(rating);
    return IntStream.rangeClosed(1, 5)
      .boxed()
      .map(i -> i <= intRating).collect(toList());
  }
}

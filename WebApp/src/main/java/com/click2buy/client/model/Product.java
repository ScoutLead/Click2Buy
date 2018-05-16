package com.click2buy.client.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @Column(name = "maker")
  private int maker;

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

  public int getMaker() {
    return maker;
  }

  public void setMaker(int maker) {
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
}

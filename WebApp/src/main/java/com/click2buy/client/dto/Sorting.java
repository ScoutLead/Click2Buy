package com.click2buy.client.dto;

public enum Sorting {
  news("creationDateTime"),
  rating("rating"),
  priceAsc("price.asc"),
  priceDesc("price.desc"),
  popular("sellingCount");

  String name;

  Sorting(String entityName) {
    this.name = entityName;
  }

  public String entityName() {
    return name;
  }
}

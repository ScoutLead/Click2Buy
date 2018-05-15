package com.click2buy.client.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent")
  private Category parent;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent")
  private List<Category> children;

  public Category() {
  }

  public Category(String name, Category parent) {
    this.name = name;
    this.parent = parent;
  }

  public Category(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getParent() {
    return parent;
  }

  public void setParent(Category parent) {
    this.parent = parent;
  }

  public List<Category> getChildren() {
    return children;
  }

  public void setChildren(List<Category> children) {
    this.children = children;
  }
}

package com.click2buy.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "regions_novaposhta")
public class Region {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "reference")
  private String reference;

  @Column(name = "areas_center")
  private String areasCenter;

  @Column(name = "updated_date_time")
  @JsonIgnore
  private LocalDateTime updatedDateTime;

  public Region() {
  }

  public Region(String name, String reference, String areasCenter,
    LocalDateTime updatedDateTime) {
    this.name = name;
    this.reference = reference;
    this.areasCenter = areasCenter;
    this.updatedDateTime = updatedDateTime;
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

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getAreasCenter() {
    return areasCenter;
  }

  public void setAreasCenter(String areasCenter) {
    this.areasCenter = areasCenter;
  }

  public LocalDateTime getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }
}

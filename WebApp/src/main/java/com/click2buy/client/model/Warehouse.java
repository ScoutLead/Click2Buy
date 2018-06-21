package com.click2buy.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Table(name = "warehouse_novaposhta")
public class Warehouse {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "site_key")
  private int siteKey;

  @ManyToOne(fetch= FetchType.LAZY)
  @JoinColumn(name = "settlement_id")
  @JsonIgnore
  private Settlement settlement;

  @Column(name = "updated_date_time")
  @JsonIgnore
  private LocalDateTime updatedDateTime;

  public int getSiteKey() {
    return siteKey;
  }

  public void setSiteKey(int siteKey) {
    this.siteKey = siteKey;
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

  public Settlement getSettlement() {
    return settlement;
  }

  public void setSettlement(Settlement settlement) {
    this.settlement = settlement;
  }

  public LocalDateTime getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }


  public static final class Builder {

    private String name;
    private int siteKey;
    private Settlement settlement;
    private LocalDateTime updatedDateTime;

    private Builder() {
    }

    public static Builder aWarehouse() {
      return new Builder();
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withSiteKey(int siteKey) {
      this.siteKey = siteKey;
      return this;
    }

    public Builder withSettlement(Settlement settlement) {
      this.settlement = settlement;
      return this;
    }

    public Builder withUpdatedDateTime(LocalDateTime updatedDateTime) {
      this.updatedDateTime = updatedDateTime;
      return this;
    }

    public Warehouse build() {
      Warehouse warehouse = new Warehouse();
      warehouse.setName(name);
      warehouse.setSiteKey(siteKey);
      warehouse.setSettlement(settlement);
      warehouse.setUpdatedDateTime(updatedDateTime);
      return warehouse;
    }
  }
}

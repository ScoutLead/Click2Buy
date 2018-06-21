package com.click2buy.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "settlements_novaposhta")
public class Settlement {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "reference")
  private String ref;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private SettlementType type;

  @Column(name = "warehouse")
  private boolean isWarehouse;

  @ManyToOne(fetch= FetchType.LAZY)
  @JoinColumn(name = "region_id")
  @JsonIgnore
  private Region region;

  @Column(name = "region_reference")
  @JsonIgnore
  private String regionReference;

  @Column(name = "updated_date_time")
  @JsonIgnore
  private LocalDateTime updatedDateTime;

  public String getRegionReference() {
    return regionReference;
  }

  public void setRegionReference(String regionReference) {
    this.regionReference = regionReference;
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

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public LocalDateTime getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  public SettlementType getType() {
    return type;
  }

  public void setType(SettlementType type) {
    this.type = type;
  }

  public boolean isWarehouse() {
    return isWarehouse;
  }

  public void setWarehouse(boolean warehouse) {
    isWarehouse = warehouse;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public static final class Builder {

    private int id;
    private String name;
    private String ref;
    private SettlementType type;
    private boolean isWarehouse;
    private Region region;
    private String regionReference;
    private LocalDateTime updatedDateTime;

    private Builder() {
    }

    public static Builder aSettlement() {
      return new Builder();
    }

    public Builder withId(int id) {
      this.id = id;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withRef(String ref) {
      this.ref = ref;
      return this;
    }

    public Builder withType(SettlementType type) {
      this.type = type;
      return this;
    }

    public Builder withIsWarehouse(boolean isWarehouse) {
      this.isWarehouse = isWarehouse;
      return this;
    }

    public Builder withRegion(Region region) {
      this.region = region;
      return this;
    }

    public Builder withRegionReference(String regionReference) {
      this.regionReference = regionReference;
      return this;
    }

    public Builder withUpdatedDateTime(LocalDateTime updatedDateTime) {
      this.updatedDateTime = updatedDateTime;
      return this;
    }

    public Settlement build() {
      Settlement settlement = new Settlement();
      settlement.setId(id);
      settlement.setName(name);
      settlement.setRef(ref);
      settlement.setType(type);
      settlement.setRegion(region);
      settlement.setUpdatedDateTime(updatedDateTime);
      settlement.regionReference = this.regionReference;
      settlement.isWarehouse = this.isWarehouse;
      return settlement;
    }
  }
}

package com.click2buy.client.model.novaPoshta;

import com.click2buy.client.model.SettlementType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Settlement {
  @JsonProperty(value = "Ref")
  private String ref;

  @JsonProperty(value = "Description")
  private String name;

  @JsonProperty(value = "SettlementType")
  @JsonDeserialize(using = SettlementDeserializer.class)
  private SettlementType type;

  @JsonProperty(value = "Warehouse")
  @JsonDeserialize(using = BooleanNovaPoshtaDeserializer.class)
  private boolean warehouse;

  @JsonProperty(value = "AreaDescription")
  private String regionRef;

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SettlementType getType() {
    return type;
  }

  public void setType(SettlementType type) {
    this.type = type;
  }

  public boolean isWarehouse() {
    return warehouse;
  }

  public void setWarehouse(boolean warehouse) {
    this.warehouse = warehouse;
  }

  public String getRegionRef() {
    return regionRef;
  }

  public void setRegionRef(String regionRef) {
    this.regionRef = regionRef;
  }
}

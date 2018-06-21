package com.click2buy.client.model.novaPoshta;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Area {

  @JsonProperty(value = "Ref")
  private String ref;

  @JsonProperty(value = "AreasCenter")
  private String areasCenter;

  @JsonProperty(value = "Description")
  private String name;

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public String getAreasCenter() {
    return areasCenter;
  }

  public void setAreasCenter(String areasCenter) {
    this.areasCenter = areasCenter;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

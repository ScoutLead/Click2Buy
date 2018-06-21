package com.click2buy.client.model.novaPoshta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Warehouse {

  @JsonProperty(value = "Ref")
  private String ref;

  @JsonProperty(value = "SiteKey")
  private int siteKey;

  @JsonProperty(value = "CityDescription")
  private String cityReference;

  @JsonProperty(value = "Description")
  private String name;

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public int getSiteKey() {
    return siteKey;
  }

  public void setSiteKey(int siteKey) {
    this.siteKey = siteKey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCityReference() {
    return cityReference;
  }

  public void setCityReference(String cityReference) {
    this.cityReference = cityReference;
  }
}

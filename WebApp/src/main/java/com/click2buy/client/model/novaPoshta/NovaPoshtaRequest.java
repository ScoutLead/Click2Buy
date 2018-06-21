package com.click2buy.client.model.novaPoshta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class NovaPoshtaRequest {
  @JsonProperty
  @NotNull
  public String apiKey;
  @JsonProperty
  @NotNull
  public String modelName;
  @JsonProperty
  @NotNull
  public String calledMethod;

  @JsonCreator
  public NovaPoshtaRequest(String apiKey, String modelName, String calledMethod) {
    this.apiKey = apiKey;
    this.modelName = modelName;
    this.calledMethod = calledMethod;
  }
}

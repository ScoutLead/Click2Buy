package com.click2buy.client.model.novaPoshta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaPoshtaResponse<T> {
  public boolean success;
  public T data;
  public Object[] errors;
  public Object[] warnings;
}
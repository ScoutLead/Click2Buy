package com.click2buy.client.model.novaPoshta;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class BooleanNovaPoshtaDeserializer extends StdDeserializer<Boolean> {

  public BooleanNovaPoshtaDeserializer(Class<?> vc) {
    super(vc);
  }

  public BooleanNovaPoshtaDeserializer() {
    this(null);
  }

  @Override
  public Boolean deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException {
    return !p.getText().equals("0");
  }
}

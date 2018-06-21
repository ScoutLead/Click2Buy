package com.click2buy.client.model.novaPoshta;

import com.click2buy.client.model.SettlementType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class SettlementDeserializer extends StdDeserializer<SettlementType> {

  public SettlementDeserializer() {
    this(null);
  }

  public SettlementDeserializer(Class<?> vc) {
    super(vc);
  }



  @Override
  public SettlementType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String type = p.getText();
    if(type.equals("563ced10-f210-11e3-8c4a-0050568002cf")) {
      return SettlementType.CITY;
    } else if (type.equals("563ced13-f210-11e3-8c4a-0050568002cf")) {
      return SettlementType.VILLAGE;
    } else {
      return SettlementType.URBAN_VILLAGE;
    }
  }
}

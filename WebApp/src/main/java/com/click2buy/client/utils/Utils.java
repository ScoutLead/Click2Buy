package com.click2buy.client.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Utils {
  public static List<String> convertToArray(JsonNode node) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectReader reader = mapper.readerFor(new TypeReference<List<String>>() {});
    try {
      return  reader.readValue(node);
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }
}

package com.click2buy.client.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.List;

public class FilterUrlBuilder {

  private final String json;
  private Range price;
  private JsonNode filterBy;

  public FilterUrlBuilder(String json) throws IOException {
    this.json = json;
    renew();
  }

  public FilterUrlBuilder renew() throws IOException {
    if(json == null) {
      filterBy = new ObjectMapper().readTree("{}");
    } else {
      filterBy = new ObjectMapper().readTree(json);
    }
    return this;
  }
  public FilterUrlBuilder add(String key, String maker) {
    List<JsonNode> parents = filterBy.findParents(key);
    if(!parents.isEmpty()) {
      ((ArrayNode)parents.get(0).get(key).get("$in")).add(maker);
    } else {
      if(filterBy.fields().hasNext()) {
        ((ObjectNode)filterBy.get("$and")).set(key, new ObjectMapper()
          .createObjectNode()
          .putArray("$in").add(maker));
      } else {
        ((ObjectNode)filterBy).set(key, new ObjectMapper()
          .createObjectNode().set("$in",
            new ObjectMapper().createArrayNode().add(maker)));
      }

    }
    return this;
  }

  public FilterUrlBuilder remove(String key, String value) {
    List<JsonNode> parents = filterBy.findParents(key);
    if(!parents.isEmpty()) {
      ArrayNode in = (ArrayNode) parents.get(0).get(key).get("$in");
      List<String> strings = Utils.convertToArray(in);
      int index = strings.indexOf(value);
      strings.remove(value);
      if(strings.isEmpty()) {
        ((ObjectNode)parents.get(0)).remove(key);
      } else {
        in.remove(index);
      }
    }
    return this;
  }

  public String build() {
    return filterBy.toString();
  }
}

package com.click2buy.client.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

public class FilterUrlBuilderTest {

  @Test
  public void addMakerTest() throws IOException {
    String result = new FilterUrlBuilder("{\n"
      + "\t\"maker\": {\n"
      + "\t\t\"$in\": [\"test\"]\n"
      + "\t}\n"
      + "}").add("maker", "rest").build();

    assertEquals("{\"maker\":{\"$in\":[\"test\",\"rest\"]}}", result);
  }

  @Test
  public void addMaker_WhenMakersIsEmptyTest() throws IOException {
    String result = new FilterUrlBuilder("{\n"
      + "\t\"$and\": {\n"
      + "\t\t\"price\": [\"test\"]\n"
      + "\t}\n"
      + "}").add("maker", "rest").build();
    assertEquals("{\"$and\":{\"price\":[\"test\"],\"maker\":[\"rest\"]}}", result);
  }

  @Test
  public void addMaker_WhenMakersIsEmptyAndJsonEmptyTest() throws IOException {
    String result = new FilterUrlBuilder("{}").add("maker", "rest").build();
    assertEquals("{\"maker\":{\"$in\":[\"rest\"]}}", result);
  }
}

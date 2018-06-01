package com.click2buy.client.utils;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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

  public static List<Integer> pageIndexes(int totalPages, int currentPage) {
    if (totalPages <= 3 && currentPage <= totalPages) {
      return generateIndexes(1, totalPages);
    }
    if (currentPage >= totalPages - 1) {
      return generateIndexes(totalPages - 2, totalPages);
    }
    if (currentPage == 1) {
      return generateIndexes(currentPage, currentPage + 2);
    }
    return generateIndexes(currentPage - 1, currentPage + 1);
  }

  private static List<Integer> generateIndexes(int first, int last) {
    return IntStream.rangeClosed(first, last).boxed().collect(toList());
  }
}

package com.click2buy.client.service;

import java.util.Optional;

public interface ImageCache {
  Optional<byte[]> get(String key);
  void add(String key, byte[] value);
}

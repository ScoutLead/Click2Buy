package com.click2buy.client.service;

import java.util.Optional;

public interface GoodsImageService {
  Optional<byte[]> download(String path);
}

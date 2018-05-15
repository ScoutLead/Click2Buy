package com.click2buy.client.service;

import java.io.InputStream;
import java.util.Optional;

public interface AwsS3Service {
  boolean upload(String key, InputStream inputStream);
  Optional<byte[]> download(String key);
}

package com.click2buy.client.service.implementation;

import com.click2buy.client.service.ImageCache;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("FileSystemImageCache")
public class FileSystemImageCache implements ImageCache {

  private Map<String, Path> paths = new HashMap<>();

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public FileSystemImageCache() {
    this.paths = new HashMap<>();
    createDirectory("head_images");
    try (Stream<Path> files = Files.walk(Paths.get("head_images"))) {
      files
        .forEach(x -> paths.put(x.getFileName().toString(), x));
    } catch (IOException e) {
      log.error("Cannot full cache", e);
    }

  }

  @Override
  public Optional<byte[]> get(String key) {
    return Optional.ofNullable(paths.get(key))
      .flatMap(this::getBytes);
  }

  private Optional<byte[]> getBytes(Path path) {
    try {
      return Optional.of(Files.readAllBytes(path));
    } catch (IOException e) {
      log.error("Cannot get image from cache", e);
      return Optional.empty();
    }
  }

  @Override
  public void add(String key, byte[] value) {
    writeFile(key, value)
      .ifPresent(path -> paths.put(key, path));
  }

  private Optional<Path> writeFile(String key, byte[] value) {
    try {
      Path path = Paths.get("head_images/" + key);
      Files.write(path, value, StandardOpenOption.CREATE);
      return Optional.of(path);
    } catch (IOException e) {
      log.error("Cannot add image to cache", e);
      return Optional.empty();
    }
  }

  private void createDirectory(String dirPath) {
    Path dirPathObj = Paths.get(dirPath);
    if (dirPathObj.toFile().exists()) {
      log.info("! Directory Already Exists !");
    } else {
      try {
        // Creating The New Directory Structure
        Files.createDirectories(dirPathObj);
        log.info("! New Directory Successfully Created !");
      } catch (IOException ioExceptionObj) {
        log.error("Problem Occured While Creating The Directory Structure= ", ioExceptionObj);
      }
    }
  }
}

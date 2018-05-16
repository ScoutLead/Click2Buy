package com.click2buy.client.service.implementation;

import com.click2buy.client.service.AwsS3Service;
import com.click2buy.client.service.GoodsImageService;
import com.click2buy.client.service.ImageCache;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("goodsImageService")
public class GoodsImageServiceImpl implements GoodsImageService {

  private final AwsS3Service awsS3Service;
  private final ImageCache cache;

  @Autowired
  public GoodsImageServiceImpl(
    @Qualifier("awsService")
      AwsS3Service awsS3Service,
    @Qualifier("FileSystemImageCache")
    ImageCache imageCache
  ) {
    this.awsS3Service = awsS3Service;
    this.cache = imageCache;
  }

  @Override
  public Optional<byte[]> download(String path) {
    Optional<byte[]> bytes = cache.get(path);
    if(bytes
      .isPresent()) {
      return bytes;
    } else {
      Optional<byte[]> download = awsS3Service.download(path);
      download.ifPresent(key -> cache.add(path, key));
      return download;
    }
  }
}

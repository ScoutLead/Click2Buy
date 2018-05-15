package com.click2buy.client.service.implementation;

import com.click2buy.client.service.AwsS3Service;
import com.click2buy.client.service.GoodsImageService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("goodsImageService")
public class GoodsImageServiceImpl implements GoodsImageService {

  private final AwsS3Service awsS3Service;

  @Autowired
  public GoodsImageServiceImpl(
    @Qualifier("awsService")
      AwsS3Service awsS3Service) {
    this.awsS3Service = awsS3Service;
  }

  @Override
  public Optional<byte[]> download(String path) {
    return awsS3Service.download(path);
  }
}

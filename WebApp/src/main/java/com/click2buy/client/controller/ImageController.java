package com.click2buy.client.controller;

import com.click2buy.client.service.GoodsImageService;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

  private final GoodsImageService imageService;

  public ImageController(GoodsImageService imageService) {
    this.imageService = imageService;
  }

  @RequestMapping(value = "image/{imageName}")
  @ResponseBody
  public ResponseEntity<byte[]> getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
    return imageService.download(imageName)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

}

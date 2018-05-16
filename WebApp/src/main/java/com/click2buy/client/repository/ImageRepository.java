package com.click2buy.client.repository;

import com.click2buy.client.model.GoodsImage;
import com.click2buy.client.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<GoodsImage, Integer> {
  GoodsImage findByMainAndProductId(boolean isMain, int productId);
}

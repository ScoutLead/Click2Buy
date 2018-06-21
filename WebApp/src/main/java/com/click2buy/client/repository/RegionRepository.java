package com.click2buy.client.repository;

import com.click2buy.client.model.Region;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("regionRepository")
public interface RegionRepository extends JpaRepository<Region, Integer> {
  CompletableFuture<List<Region>> getAllBy();

  Region findByName(String name);
}

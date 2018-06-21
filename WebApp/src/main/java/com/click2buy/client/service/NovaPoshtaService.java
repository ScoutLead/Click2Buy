package com.click2buy.client.service;

import com.click2buy.client.model.Region;
import com.click2buy.client.model.Settlement;
import com.click2buy.client.model.Warehouse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

public interface NovaPoshtaService {

  @Async
  CompletableFuture<List<Settlement>> settlements(Integer id);

  @Async
  CompletableFuture<List<Warehouse>> warehouses(Long settlementId);

  @Async
  CompletableFuture<List<Region>> regions();
}

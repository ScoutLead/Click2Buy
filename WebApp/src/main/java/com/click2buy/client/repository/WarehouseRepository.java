package com.click2buy.client.repository;

import com.click2buy.client.model.Region;
import com.click2buy.client.model.Warehouse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("warehouseRepository")
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
  CompletableFuture<List<Warehouse>> getAllBySettlement_Id(Integer id);
}

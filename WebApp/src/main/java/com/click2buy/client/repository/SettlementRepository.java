package com.click2buy.client.repository;

import com.click2buy.client.model.Region;
import com.click2buy.client.model.Settlement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("settlementRepository")
public interface SettlementRepository extends JpaRepository<Settlement, Integer> {
  CompletableFuture<List<Settlement>> getAllByRegion_Id(Integer id);

  List<Settlement> findByName(String name);
}

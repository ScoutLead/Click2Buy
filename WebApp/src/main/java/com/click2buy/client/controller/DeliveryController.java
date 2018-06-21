package com.click2buy.client.controller;

import com.click2buy.client.model.Region;
import com.click2buy.client.model.Settlement;
import com.click2buy.client.model.Warehouse;
import com.click2buy.client.model.novaPoshta.Area;
import com.click2buy.client.service.NovaPoshtaService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeliveryController {

  private final NovaPoshtaService novaPoshtaService;

  public DeliveryController(NovaPoshtaService novaPoshtaService) {
    this.novaPoshtaService = novaPoshtaService;
  }

  @RequestMapping(value = "/regions")
  @ResponseBody
  public CompletableFuture<ResponseEntity<List<Region>>> getRegions() {
    return novaPoshtaService.regions().thenApply(ResponseEntity::ok);
  }

  @RequestMapping(value = "/regions/{ref}/settlements")
  @ResponseBody
  public CompletableFuture<ResponseEntity<List<Settlement>>> getSettlements(
    @PathVariable(value = "ref") Integer ref) {
    return novaPoshtaService.settlements(ref).thenApply(ResponseEntity::ok);
  }

  @RequestMapping(value = "/settlements/{wref}/warehouses")
  @ResponseBody
  public CompletableFuture<ResponseEntity<List<Warehouse>>> getWarehouses(
    @PathVariable(value = "wref") Long settlementId) {
    return novaPoshtaService.warehouses(settlementId).thenApply(ResponseEntity::ok);
  }

}

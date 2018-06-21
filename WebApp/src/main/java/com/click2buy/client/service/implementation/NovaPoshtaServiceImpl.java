package com.click2buy.client.service.implementation;

import static com.click2buy.client.model.Settlement.Builder.aSettlement;
import static com.click2buy.client.model.Warehouse.Builder.aWarehouse;
import static java.util.stream.Collectors.toList;

import com.click2buy.client.model.Region;
import com.click2buy.client.model.Settlement;
import com.click2buy.client.model.Warehouse;
import com.click2buy.client.model.novaPoshta.NovaPoshtaRequest;
import com.click2buy.client.model.novaPoshta.AreaResponse;
import com.click2buy.client.model.novaPoshta.SettlementResponse;
import com.click2buy.client.model.novaPoshta.WarehouseResponse;
import com.click2buy.client.repository.RegionRepository;
import com.click2buy.client.repository.SettlementRepository;
import com.click2buy.client.repository.WarehouseRepository;
import com.click2buy.client.service.NovaPoshtaService;
import com.click2buy.client.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

@Service("novaPoshtaService")
public class NovaPoshtaServiceImpl implements NovaPoshtaService {

  private final String key;
  private final RegionRepository regionRepository;
  private final SettlementRepository settlementRepository;
  private final WarehouseRepository warehouseRepository;

  @Autowired
  public NovaPoshtaServiceImpl(
    Environment env,
    @Qualifier("regionRepository") RegionRepository regionRepository,
    @Qualifier("settlementRepository") SettlementRepository settlementRepository,
    @Qualifier("warehouseRepository") WarehouseRepository warehouseRepository) {
    key = env.getProperty("novaPoshta.key");
    this.regionRepository = regionRepository;
    this.settlementRepository = settlementRepository;
    this.warehouseRepository = warehouseRepository;
  }

  @Override
  @Async
  public CompletableFuture<List<Settlement>> settlements(Integer id) {
    return settlementRepository.getAllByRegion_Id(id)
      .thenApply(x ->
        x.isEmpty() ||
          x.get(0).getUpdatedDateTime().getMonthValue() == LocalDateTime.now().getMonthValue() - 1 ?
          Optional.<List<Settlement>>empty() : Optional.of(x)
      ).thenCompose(list ->
        list
          .map(CompletableFuture::completedFuture)
          .orElseGet(() ->
            getSettlementsFromNovaPoshta()
              .thenApply(this::convertToSettlements).thenCompose(x -> {
              settlementRepository.deleteAll();
              settlementRepository.save(x);
              return settlementRepository.getAllByRegion_Id(id);
            })
          )
      );
  }

  @Override
  @Async
  public CompletableFuture<List<Warehouse>> warehouses(Long settlementId) {
    return warehouseRepository.getAllBySettlement_Id(settlementId.intValue())
      .thenApply(x ->
        x.isEmpty() ||
          x.get(0).getUpdatedDateTime().getMonthValue() == LocalDateTime.now().getMonthValue() - 1 ?
          Optional.<List<Warehouse>>empty() : Optional.of(x)
      ).thenCompose(list ->
        list
          .map(CompletableFuture::completedFuture)
          .orElseGet(() ->
            getWarehousesFromNovaPoshta()
              .thenApply(this::convertToWarehouses).thenCompose(x -> {
              warehouseRepository.deleteAll();
              warehouseRepository.save(x);
              return warehouseRepository.getAllBySettlement_Id(settlementId.intValue());
            })
          )
      );
  }

  @Override
  @Async
  public CompletableFuture<List<Region>> regions() {
    return regionRepository.getAllBy().thenApply(x ->
       x.isEmpty() ||
         x.get(0).getUpdatedDateTime().getMonthValue() == LocalDateTime.now().getMonthValue() - 1?
         Optional.<List<Region>>empty(): Optional.of(x)
    ).thenCompose(list ->
      list
        .map(CompletableFuture::completedFuture)
        .orElseGet(() -> getAreasFromNovaPoshta()
            .thenApply(this::convertToRegions)
            .thenApply(x -> {
              regionRepository.deleteAll();
              regionRepository.save(x);
              return x;
            })
        )
    );
  }

  private List<Region> convertToRegions(ResponseEntity<AreaResponse> x) {
    return Stream.of(x.getBody().data).map(
      area -> new Region(area.getName(), area.getRef(), area.getAreasCenter(),
        LocalDateTime.now())).collect(
      toList());
  }

  private List<Settlement> convertToSettlements(List<SettlementResponse> x) {
    return Stream.of(x).flatMap(Collection::stream)
      .flatMap(a -> Stream.of(a.data)).map(
      area -> aSettlement()
        .withRegionReference(area.getRegionRef())
        .withRef(area.getRef())
        .withName(area.getName())
        .withRegion(regionRepository.findByName(area.getRegionRef().split(" ")[0]))
        .withIsWarehouse(area.isWarehouse())
        .withType(area.getType())
        .withUpdatedDateTime(LocalDateTime.now())
        .build())
      .collect(
        toList());
  }

  private List<Warehouse> convertToWarehouses(ResponseEntity<WarehouseResponse> x) {
    return Stream.of(x.getBody().data).map(
      warehouse -> aWarehouse()
        .withName(warehouse.getName())
        .withSiteKey(warehouse.getSiteKey())
        .withSettlement(findByName(warehouse.getCityReference()))
        .withUpdatedDateTime(LocalDateTime.now())
        .build())
      .collect(
        toList());
  }

  // TODO: 6/21/2018 Rewrite
  private Settlement findByName(String name) {
    String[] split = name.split(" *\\(");
    List<Settlement> byName = settlementRepository.findByName(split[0]);
    Map<String, String> regions = new HashMap<>();
    regions.put("Чернівці", "Чернівецька");

    if(byName.isEmpty()) return null;
    else {
      List<Settlement> collect = byName.stream().filter(Settlement::isWarehouse).collect(toList());
      if(collect.size() == 1) {
        return collect.get(0);
      } else {
        System.out.println(name);
        if(split.length == 1) {
          return collect.stream()
            .filter(x -> x.getRegionReference()
              .contains(regions.getOrDefault(split[0], split[0])))
            .findAny().get();
        } else {

          System.out.println(convertToRegion(split[1]));
        if(name.contains("Миколаївка (Слов’янська міська рада)")) {
          return collect.stream()
            .filter(x -> x.getRegionReference()
              .contains("Донецьк"))
            .findAny().get();
        }
          return collect.stream()
            .filter(x -> x.getRegionReference()
              .contains(convertToRegion(split[1])))
            .findAny().get();
        }
      }
    }
  }

  private String convertToRegion(String name) {
    return name.replaceAll("\\)| |,|(.*( |\\.)(р-н(\\.*)|район))|((\\.*)( *)обл(\\.*))|смт\\.*|райц|м\\.", "");
  }

  private CompletableFuture<ResponseEntity<AreaResponse>> getAreasFromNovaPoshta() {
    return Utils
      .buildCompletableFuture(new AsyncRestTemplate().postForEntity(
        "https://api.novaposhta.ua/v2.0/json/",
        new HttpEntity<>(new NovaPoshtaRequest(key, "Address", "getAreas")),
        AreaResponse.class));
  }

  private CompletableFuture<List<SettlementResponse>> getSettlementsFromNovaPoshta() {

    ObjectMapper mapper = new ObjectMapper();
    return CompletableFuture.supplyAsync(() -> {
      List<Path> paths  = null;
      try (Stream<Path> files = Files.walk(Paths.get("settlements"))) {
        paths =   files.collect(toList());
      } catch (IOException e) {
        System.out.println(e);
      }

      return paths.stream().filter(x -> x.toFile().isFile()).map(x -> {
        try {
          return mapper.readValue(x.toFile(), SettlementResponse.class);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }).collect(toList());
    });

//    return Utils
//      .buildCompletableFuture(new AsyncRestTemplate().postForEntity(
//        "https://api.novaposhta.ua/v2.0/json/",
//        new HttpEntity<>(new NovaPoshtaRequest(key, "AddressGeneral", "getSettlements")),
//        SettlementResponse.class));
  }

  private CompletableFuture<ResponseEntity<WarehouseResponse>> getWarehousesFromNovaPoshta() {
    return Utils
      .buildCompletableFuture(new AsyncRestTemplate().postForEntity(
        "https://api.novaposhta.ua/v2.0/json/",
        new HttpEntity<>(new NovaPoshtaRequest(key, "AddressGeneral", "getWarehouses")),
        WarehouseResponse.class));
  }
}

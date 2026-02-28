package com.cloudnative.inventory_service.service;

import com.cloudnative.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Cacheable(value = "inventory", key = "#skuCode")
    public boolean isInStock(String skuCode) {
        log.info("Slowly request to PosgreSQL for item {}", skuCode);

        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventory -> inventory.getQuantity() > 0)
                .orElse(false);
    }
}

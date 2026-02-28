package com.cloudnative.inventory_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void handleOrderEvent(String orderNumber) {
        log.info("New message from kafka topic, number {}", orderNumber);

        String testSkuCode = "IPHONE-15-PRO";
        boolean inStock = inventoryService.isInStock(testSkuCode);

        if (inStock) {
            log.info("Inventory in stock {}", orderNumber);
        } else {
            log.info("Inventory not is stock {}", orderNumber);
        }

        log.info("Checking the stock for the order {}", orderNumber);
    }
}

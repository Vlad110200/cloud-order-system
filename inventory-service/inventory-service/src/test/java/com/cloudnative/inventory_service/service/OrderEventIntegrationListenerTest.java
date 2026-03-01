package com.cloudnative.inventory_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
public class OrderEventIntegrationListenerTest {

    private static final String TOPIC = "order-topic";

    @Container
    static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15-alpine")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);

        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void handleOrderEvent_receivesMessage_andChecksInventory() {
        String testOrderNumber = "ORD-12345";
        String hardcodedSku = "IPHONE-15-PRO";

        when(inventoryService.isInStock(hardcodedSku)).thenReturn(true);

        kafkaTemplate.send(TOPIC, testOrderNumber);

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    verify(inventoryService).isInStock(hardcodedSku);
                });
    }
}

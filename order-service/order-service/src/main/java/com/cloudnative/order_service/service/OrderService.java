package com.cloudnative.order_service.service;

import com.cloudnative.order_service.dto.OrderRequest;
import com.cloudnative.order_service.model.Order;
import com.cloudnative.order_service.model.OrderStatus;
import com.cloudnative.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDING);
        order.setSkuCode(orderRequest.skuCode());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());

        orderRepository.save(order);
        log.info("Order {} saved in database", order.getOrderNumber());

        kafkaTemplate.send("order-topic", order.getOrderNumber());
        log.info("Message sent to kafka in topic 'order-topic'");
    }
}

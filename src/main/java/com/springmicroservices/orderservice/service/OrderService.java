package com.springmicroservices.orderservice.service;

import com.springmicroservices.orderservice.model.OrderRequest;
import com.springmicroservices.orderservice.model.OrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    Optional<OrderResponse> getOrderById(Long orderId);

    ResponseEntity<List<OrderResponse>> getAllOrder();
}

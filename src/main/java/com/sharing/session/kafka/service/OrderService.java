package com.sharing.session.kafka.service;

import java.util.List;

import com.sharing.session.kafka.entity.Order;
import com.sharing.session.kafka.model.request.ProcessOrderRequest;

import reactor.core.publisher.Mono;

public interface OrderService {
  Mono<List<Order>> getAllOrders();

  Mono<Void> sendProcessOrderEvent(ProcessOrderRequest request);

  Mono<Void> processOrder(ProcessOrderRequest request);
}

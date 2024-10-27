package com.sharing.session.kafka.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.sharing.session.kafka.entity.Order;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}

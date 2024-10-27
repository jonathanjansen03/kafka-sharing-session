package com.sharing.session.kafka.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.sharing.session.kafka.entity.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}

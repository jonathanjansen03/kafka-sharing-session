package com.sharing.session.kafka.service;

import java.util.List;

import com.sharing.session.kafka.entity.Product;
import com.sharing.session.kafka.model.request.ProductRequest;

import reactor.core.publisher.Mono;

public interface ProductService {
  Mono<Product> createProduct(ProductRequest request);

  Mono<List<Product>> getAllProducts();

  Mono<Product> updateProduct(String id, ProductRequest request);

  Mono<Void> deleteProduct(String id);
}

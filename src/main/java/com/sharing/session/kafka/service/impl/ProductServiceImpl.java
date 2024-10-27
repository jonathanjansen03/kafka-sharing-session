package com.sharing.session.kafka.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sharing.session.kafka.entity.Product;
import com.sharing.session.kafka.exception.DataNotFoundException;
import com.sharing.session.kafka.helper.LogHelper;
import com.sharing.session.kafka.model.request.ProductRequest;
import com.sharing.session.kafka.repository.ProductRepository;
import com.sharing.session.kafka.service.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public Mono<Product> createProduct(ProductRequest request) {
    return Mono.just(request).map(this::toProductEntity).flatMap(productRepository::save)
        .doOnSuccess(product -> LogHelper.success(LogHelper.ID, product.getId()))
        .doOnError(e -> LogHelper.error(LogHelper.REQUEST, request, e));
  }

  @Override
  public Mono<List<Product>> getAllProducts() {
    return this.productRepository.findAll().collectList()
        .doOnSuccess(products -> LogHelper.success(LogHelper.COUNT, products.size()))
        .doOnError(LogHelper::error);
  }

  @Override
  public Mono<Product> updateProduct(String id, ProductRequest request) {
    return this.findProductById(id).map(product -> this.updateProductData(product, request))
        .doOnSuccess(user -> LogHelper.success(LogHelper.ID, id))
        .doOnError(e -> LogHelper.error(LogHelper.ID, id, e));
  }

  @Override
  public Mono<Void> deleteProduct(String id) {
    return this.findProductById(id).flatMap(product -> this.productRepository.deleteById(id))
        .doOnSuccess(product -> LogHelper.success(LogHelper.ID, id))
        .doOnError(e -> LogHelper.error(LogHelper.ID, id, e));
  }

  private Product toProductEntity(ProductRequest request) {
    return Product.builder().name(request.getName()).price(request.getPrice()).build();
  }

  private Mono<Product> findProductById(String id) {
    return this.productRepository.findById(id)
        .switchIfEmpty(Mono.defer(
            () -> Mono.error(new DataNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                LogHelper.ID.toLowerCase(Locale.ROOT)))));
  }

  private Product updateProductData(Product product, ProductRequest request) {
    return product.toBuilder().name(request.getName()).price(request.getPrice()).build();
  }
}

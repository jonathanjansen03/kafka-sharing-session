package com.sharing.session.kafka.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sharing.session.kafka.constant.KafkaConstant;
import com.sharing.session.kafka.entity.Order;
import com.sharing.session.kafka.entity.Product;
import com.sharing.session.kafka.entity.User;
import com.sharing.session.kafka.exception.DataNotFoundException;
import com.sharing.session.kafka.helper.JsonMapper;
import com.sharing.session.kafka.helper.KafkaHelper;
import com.sharing.session.kafka.helper.LogHelper;
import com.sharing.session.kafka.model.request.ProcessOrderRequest;
import com.sharing.session.kafka.repository.OrderRepository;
import com.sharing.session.kafka.repository.ProductRepository;
import com.sharing.session.kafka.repository.UserRepository;
import com.sharing.session.kafka.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final JsonMapper jsonMapper;
  private final KafkaHelper kafkaHelper;

  @Override
  public Mono<List<Order>> getAllOrders() {
    return this.orderRepository.findAll().collectList()
        .doOnSuccess(orders -> LogHelper.success(LogHelper.COUNT, orders.size()))
        .doOnError(LogHelper::error);
  }

  @Override
  public Mono<Void> sendProcessOrderEvent(ProcessOrderRequest request) {
    return Mono.just(request).map(this.jsonMapper::toJson)
        .doOnNext(
            eventData -> this.kafkaHelper.sendEvent(KafkaConstant.Topic.PROCESS_ORDER, eventData))
        .doOnNext(eventData -> LogHelper.success(LogHelper.DATA, eventData))
        .doOnError(e -> LogHelper.error(LogHelper.DATA, request, e)).then();
  }

  @Override
  public Mono<Void> processOrder(ProcessOrderRequest request) {
    return Mono.just(request.getUserId()).flatMap(this::findUserById)
        .zipWith(this.findAllProductsByIds(request.getProductIdsAndAmounts()))
        .map(userAndProducts -> this.toOrderEntity(userAndProducts,
            request.getProductIdsAndAmounts()))
        .flatMap(this.orderRepository::save)
        .doOnSuccess(order -> LogHelper.success(LogHelper.ID, order.getId()))
        .doOnError(LogHelper::error).then();
  }

  private Mono<User> findUserById(String userId) {
    return this.userRepository.findById(userId).switchIfEmpty(Mono.defer(() -> Mono
        .error(new DataNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase(), LogHelper.ID))));
  }

  private Mono<List<Product>> findAllProductsByIds(Map<String, Integer> productIdsAndAmounts) {
    return this.productRepository.findAllById(productIdsAndAmounts.keySet()).collectList()
        .filter(CollectionUtils::isNotEmpty).switchIfEmpty(Mono.defer(() -> Mono.error(
            new DataNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase(), LogHelper.ID))));
  }

  private Order toOrderEntity(Tuple2<User, List<Product>> userAndProducts,
      Map<String, Integer> productIdsAndAmounts) {
    List<Order.Item> items = this.toOrderItems(userAndProducts.getT2(), productIdsAndAmounts);
    return Order.builder().user(userAndProducts.getT1()).items(items)
        .totalQuantity(this.getTotalQuantity(items)).totalPayment(this.getTotalPayment(items))
        .build();
  }

  private List<Order.Item> toOrderItems(List<Product> products,
      Map<String, Integer> productIdsAndAmounts) {
    return products.stream().map(product -> Order.Item.builder().product(product)
        .quantity(productIdsAndAmounts.get(product.getId())).build()).toList();
  }

  private int getTotalQuantity(List<Order.Item> items) {
    return items.stream().mapToInt(Order.Item::getQuantity).sum();
  }

  private double getTotalPayment(List<Order.Item> items) {
    return items.stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
        .sum();
  }
}

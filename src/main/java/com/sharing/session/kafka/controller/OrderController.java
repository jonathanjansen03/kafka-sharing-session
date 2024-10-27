package com.sharing.session.kafka.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharing.session.kafka.constant.ApiPath;
import com.sharing.session.kafka.entity.Order;
import com.sharing.session.kafka.helper.ResponseHelper;
import com.sharing.session.kafka.model.request.ProcessOrderRequest;
import com.sharing.session.kafka.model.response.WebResponse;
import com.sharing.session.kafka.service.OrderService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.ORDERS)
public class OrderController {
  private final OrderService orderService;

  @GetMapping
  public Mono<WebResponse<List<Order>>> getAllOrders() {
    return this.orderService.getAllOrders().map(ResponseHelper::ok);
  }

  @PostMapping("/_process")
  public Mono<WebResponse<Void>> processOrder(@RequestBody ProcessOrderRequest request) {
    return this.orderService.sendProcessOrderEvent(request).thenReturn(ResponseHelper.ok());
  }
}

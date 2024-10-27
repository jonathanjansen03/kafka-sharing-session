package com.sharing.session.kafka.listener;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import com.sharing.session.kafka.constant.KafkaConstant;
import com.sharing.session.kafka.helper.JsonMapper;
import com.sharing.session.kafka.model.request.ProcessOrderRequest;
import com.sharing.session.kafka.service.OrderService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class ProcessOrderListener implements MessageListener<String, String> {
  private final JsonMapper jsonMapper;
  private final OrderService orderService;

  @Override
  @KafkaListener(groupId = KafkaConstant.GROUP_ID, topics = KafkaConstant.Topic.PROCESS_ORDER)
  public void onMessage(@NonNull ConsumerRecord<String, String> data) {
    Optional.of(data).map(ConsumerRecord::value)
        .map(messageJson -> this.jsonMapper.fromJson(messageJson, ProcessOrderRequest.class))
        .ifPresent(message -> this.orderService.processOrder(message)
            .subscribeOn(Schedulers.boundedElastic()).subscribe());
  }
}

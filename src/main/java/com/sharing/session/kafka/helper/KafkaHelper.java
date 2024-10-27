package com.sharing.session.kafka.helper;

import java.util.Objects;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHelper {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendEvent(String topic, String message) {
    this.kafkaTemplate.send(topic, message).whenComplete(this::logResult);
  }

  private void logResult(SendResult<String, String> result, Throwable e) {
    if (Objects.nonNull(e)) {
      log.error("#sendEvent ERROR for topic {} and message {}", result.getProducerRecord().topic(),
          result.getProducerRecord().value(), e);
    }
  }
}

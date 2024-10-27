package com.sharing.session.kafka.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaConstant {
  public static final String GROUP_ID = "com.sharing.session.kafka";

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Topic {
    public static final String PROCESS_ORDER = "com.sharing.session.kafka.process.order";
  }
}

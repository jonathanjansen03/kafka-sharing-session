package com.sharing.session.kafka.model.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessOrderRequest {
  private String userId;
  private Map<String, Integer> productIdsAndAmounts;
}

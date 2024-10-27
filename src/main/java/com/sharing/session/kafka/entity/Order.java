package com.sharing.session.kafka.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sharing.session.kafka.constant.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Collection.ORDER)
public class Order {
  @Id
  private String id;
  private User user;
  private List<Item> items;
  private int totalQuantity;
  private double totalPayment;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {
    private Product product;
    private int quantity;
  }
}

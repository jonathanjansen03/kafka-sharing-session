package com.sharing.session.kafka.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPath {
  public static final String ORDERS = "/orders";
  public static final String USERS = "/users";
  public static final String PRODUCTS = "/products";

  public static final String ID = "/{id}";
}

package com.sharing.session.kafka.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {
  private final String field;

  public DataNotFoundException(String message, String field) {
    super(message);
    this.field = field;
  }
}

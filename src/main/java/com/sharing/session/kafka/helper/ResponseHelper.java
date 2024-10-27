package com.sharing.session.kafka.helper;

import org.springframework.http.HttpStatus;

import com.sharing.session.kafka.model.response.WebResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHelper {
  public static <T> WebResponse<T> ok() {
    return toWebResponse(HttpStatus.OK, null);
  }

  public static <T> WebResponse<T> ok(T data) {
    return toWebResponse(HttpStatus.OK, data);
  }

  private static <T> WebResponse<T> toWebResponse(HttpStatus httpStatus, T data) {
    return WebResponse.<T>builder().code(httpStatus.value()).status(httpStatus.getReasonPhrase())
        .data(data).build();
  }
}

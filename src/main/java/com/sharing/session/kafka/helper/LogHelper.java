package com.sharing.session.kafka.helper;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogHelper {
  public static final String ID = "ID";
  public static final String REQUEST = "request";
  public static final String DATA = "data";
  public static final String COUNT = "count";

  private static final int CALLER_INDEX = 4;
  private static final String DOLLAR = "$";
  private static final String DEFAULT_METHOD_NAME = "operation";

  public static <T> void success(String variable, T value) {
    log.info("#{} SUCCESS with {} {}", getCallerMethodName(), variable, value);
  }

  public static <T> void error(Throwable throwable) {
    log.error("#{} ERROR", getCallerMethodName(), throwable);
  }

  public static <T> void error(String variable, T value, Throwable throwable) {
    log.error("#{} ERROR for {} {}", getCallerMethodName(), variable, value, throwable);
  }

  private static String getCallerMethodName() {
    return Optional.ofNullable(StackWalker.getInstance())
        .flatMap(stackWalker -> stackWalker
            .walk(stackFrameStream -> stackFrameStream.skip(CALLER_INDEX).findFirst()))
        .map(StackWalker.StackFrame::getMethodName).map(LogHelper::cleanMethodName)
        .orElse(DEFAULT_METHOD_NAME);
  }

  private static String cleanMethodName(String methodName) {
    if (methodName.contains(DOLLAR)) {
      return methodName.substring(methodName.indexOf(DOLLAR) + 1, methodName.lastIndexOf(DOLLAR));
    }
    return methodName;
  }
}

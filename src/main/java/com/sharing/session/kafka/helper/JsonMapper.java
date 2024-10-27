package com.sharing.session.kafka.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonMapper {
  private final ObjectMapper objectMapper;

  public <T> T fromJson(String json, Class<T> clazz) {
    try {
      objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("#convertFromJSON ERROR for JSON {}", json, e);
      return null;
    }
  }

  public String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("#convertToJson ERROR for object {}", object, e);
      return StringUtils.EMPTY;
    }
  }
}

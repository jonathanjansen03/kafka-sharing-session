package com.sharing.session.kafka.service;

import java.util.List;

import com.sharing.session.kafka.entity.User;
import com.sharing.session.kafka.model.request.UserRequest;

import reactor.core.publisher.Mono;

public interface UserService {
  Mono<User> createUser(UserRequest request);

  Mono<List<User>> getAllUsers();

  Mono<User> updateUser(String id, UserRequest request);

  Mono<Void> deleteUser(String id);
}

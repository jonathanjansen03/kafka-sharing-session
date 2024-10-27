package com.sharing.session.kafka.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sharing.session.kafka.entity.User;
import com.sharing.session.kafka.exception.DataNotFoundException;
import com.sharing.session.kafka.helper.LogHelper;
import com.sharing.session.kafka.model.request.UserRequest;
import com.sharing.session.kafka.repository.UserRepository;
import com.sharing.session.kafka.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public Mono<User> createUser(UserRequest request) {
    return Mono.just(request).map(this::toUserEntity).flatMap(this.userRepository::save)
        .doOnSuccess(user -> LogHelper.success(LogHelper.ID, user.getId()))
        .doOnError(e -> LogHelper.error(LogHelper.REQUEST, request, e));
  }

  @Override
  public Mono<List<User>> getAllUsers() {
    return this.userRepository.findAll().collectList()
        .doOnSuccess(users -> LogHelper.success(LogHelper.COUNT, users.size()))
        .doOnError(LogHelper::error);
  }

  @Override
  public Mono<User> updateUser(String id, UserRequest request) {
    return this.findUserById(id).map(user -> this.updateUserData(user, request))
        .flatMap(this.userRepository::save).doOnSuccess(user -> LogHelper.success(LogHelper.ID, id))
        .doOnError(e -> LogHelper.error(LogHelper.ID, id, e));
  }

  @Override
  public Mono<Void> deleteUser(String id) {
    return this.findUserById(id).flatMap(user -> this.userRepository.deleteById(id))
        .doOnSuccess(user -> LogHelper.success(LogHelper.ID, id))
        .doOnError(e -> LogHelper.error(LogHelper.ID, id, e));
  }

  private User toUserEntity(UserRequest request) {
    return User.builder().name(request.getName()).email(request.getEmail()).build();
  }

  private Mono<User> findUserById(String id) {
    return this.userRepository.findById(id)
        .switchIfEmpty(Mono.defer(
            () -> Mono.error(new DataNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase(),
                LogHelper.ID.toLowerCase(Locale.ROOT)))));
  }

  private User updateUserData(User user, UserRequest request) {
    return user.toBuilder().name(request.getName()).email(request.getEmail()).build();
  }
}

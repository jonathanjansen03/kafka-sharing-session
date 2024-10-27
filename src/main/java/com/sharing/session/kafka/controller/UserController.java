package com.sharing.session.kafka.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharing.session.kafka.constant.ApiPath;
import com.sharing.session.kafka.entity.User;
import com.sharing.session.kafka.helper.ResponseHelper;
import com.sharing.session.kafka.model.request.UserRequest;
import com.sharing.session.kafka.model.response.WebResponse;
import com.sharing.session.kafka.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.USERS)
public class UserController {
  private final UserService userService;

  @PostMapping
  public Mono<WebResponse<User>> createUser(@RequestBody UserRequest request) {
    return this.userService.createUser(request).map(ResponseHelper::ok);
  }

  @GetMapping
  public Mono<WebResponse<List<User>>> getAllUsers() {
    return this.userService.getAllUsers().map(ResponseHelper::ok);
  }

  @PutMapping(ApiPath.ID)
  public Mono<WebResponse<User>> updateUser(@PathVariable String id,
      @RequestBody UserRequest user) {
    return this.userService.updateUser(id, user).map(ResponseHelper::ok);
  }

  @DeleteMapping(ApiPath.ID)
  public Mono<WebResponse<Void>> deleteUser(@PathVariable String id) {
    return this.userService.deleteUser(id).thenReturn(ResponseHelper.ok());
  }
}

package com.sharing.session.kafka.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.sharing.session.kafka.entity.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}

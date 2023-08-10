package com.mycelium.local.repository.user;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface UserRepo extends CrudRepository<User, Integer> {
    List<User> findByEmail(String email);
}
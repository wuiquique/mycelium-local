package com.mycelium.local.repository.errorlog;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface ErrorLogRepo extends CrudRepository<ErrorLog, Integer> {
}

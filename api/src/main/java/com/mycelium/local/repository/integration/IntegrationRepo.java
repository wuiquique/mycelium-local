package com.mycelium.local.repository.integration;

import java.util.List;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface IntegrationRepo extends CrudRepository<Integration, Integer> {
    @Join(value = "cartInteg.user", type = Join.Type.FETCH)
    @Join(value = "cartInteg", type = Join.Type.FETCH)
    List<Integration> findByCartIntegUserId(int userId);
}

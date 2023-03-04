package com.mycelium.local.repository.role;

import java.util.List;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface RoleRepo extends GenericRepository<Role, Integer> {
    @Query("SELECT * FROM \"userRole\"")
    List<Role> findAll();
}
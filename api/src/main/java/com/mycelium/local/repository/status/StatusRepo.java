package com.mycelium.local.repository.status;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface StatusRepo extends GenericRepository<Status, Integer> {

    @Query("SELECT * FROM \"status\" WHERE id = :id")
    Optional<Status> findById(Integer id);

    @Query("SELECT * FROM \"status\"")
    List<Status> findAll();

    @Transactional
    @Query("INSERT INTO \"status\"(\"name\") VALUES(:name)")
    void create(String name);

}

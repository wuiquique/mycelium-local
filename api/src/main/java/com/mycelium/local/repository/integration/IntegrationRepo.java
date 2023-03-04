package com.mycelium.local.repository.integration;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface IntegrationRepo extends GenericRepository<Integration, Integer> {

    @Query("SELECT * FROM \"integrations\" WHERE id = :id")
    Optional<Integration> findById(Integer id);

    @Query("SELECT * FROM \"integrations\"")
    List<Integration> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"integrations\"(\"name\", \"request\", \"user\", \"password\") VALUES(:name, :request, :user, :password)")
    void create(String name, String request, String user, String password);
}

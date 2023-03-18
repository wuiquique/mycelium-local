package com.mycelium.local.repository.errorlog;

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
public interface ErrorLogRepo extends GenericRepository<ErrorLog, Integer> {

    @Query("SELECT * FROM \"errorLog\" WHERE id = :id")
    Optional<ErrorLog> findById(Integer id);

    @Query("SELECT * FROM \"errorLog\"")
    List<ErrorLog> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"errorLog\"(\"message\", \"jsonLogId\") VALUES(:message, :jsonLogId)")
    void create(String message, int jsonLogId);
}

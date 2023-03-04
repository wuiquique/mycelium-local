package com.mycelium.local.repository.jsonlog;

import java.util.Date;
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
public interface JsonLogRepo extends GenericRepository<JsonLog, Integer> {

    @Query("SELECT * FROM \"jsonLog\" WHERE id = :id")
    Optional<JsonLog> findById(Integer id);

    @Query("SELECT * FROM \"jsonLog\"")
    List<JsonLog> findAll();

    @Transactional
    @Query("INSERT INTO \"jsonLog\"(\"userId\", \"when\", \"operation\", \"archiveName\", \"success\", \"error\") VALUES(:userId, :when, :operation, :archiveName, :success, :error)")
    void create(int userId, Date when, boolean operation, String archiveName, int success, int error);
}

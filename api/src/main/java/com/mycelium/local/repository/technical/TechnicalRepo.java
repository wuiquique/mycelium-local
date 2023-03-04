package com.mycelium.local.repository.technical;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Repository("default")
@JdbcRepository
public interface TechnicalRepo extends GenericRepository<Technical, Integer> {

    @Query("SELECT * FROM \"technical\" WHERE id = :id")
    Optional<Technical> findById(Integer id);

    @Query("SELECT * FROM \"technical\"")
    List<Technical> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"technical\"(\"type\", \"value\", \"productId\") VALUES(:url, :value, :productId)")
    void create(String url, String value, int productId);
}

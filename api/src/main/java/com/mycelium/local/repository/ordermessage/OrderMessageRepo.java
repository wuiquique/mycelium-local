package com.mycelium.local.repository.ordermessage;

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
public interface OrderMessageRepo extends GenericRepository<OrderMessage, Integer> {

    @Query("SELECT * FROM \"orderMessages\" WHERE id = :id")
    Optional<OrderMessage> findById(Integer id);

    @Query("SELECT * FROM \"orderMessages\"")
    List<OrderMessage> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"orderMessages\"(\"orderId\", \"statusId\", \"message\") VALUES(:orderId, :statusId, :message)")
    void create(int orderId, int statusId, String message);

}

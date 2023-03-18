package com.mycelium.local.repository.order;

import java.util.Date;
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
public interface OrderRepo extends GenericRepository<Order, Integer> {

    @Query("SELECT * FROM \"orders\" WHERE id = :id")
    Optional<Order> findById(Integer id);

    @Query("SELECT * FROM \"orders\"")
    List<Order> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"orders\"(\"userId\", \"direction\", \"state\", \"city\", \"zip\", \"phone\", \"since\", \"till\") VALUES(:userId, :direction, :state, :city, :zip, :phone, :since, :till)")
    void create(int userId, String direction, String state, String city, int zip, int phone, Date since, Date till);

}

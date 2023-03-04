package com.mycelium.local.repository.cart;

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
public interface CartRepo extends GenericRepository<Cart, Integer> {

    @Query("SELECT * FROM \"carts\" WHERE id = :id")
    Optional<Cart> findById(Integer id);

    @Query("SELECT * FROM \"carts\"")
    List<Cart> findAll();

    @Transactional
    @Query("INSERT INTO \"carts\"(\"productId\", \"quantity\", \"userId\") VALUES(:productId, :quantity, :userId)")
    void create(int productId, int quantity, int userId);
}

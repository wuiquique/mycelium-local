package com.mycelium.local.repository.cart;

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
public interface CartRepo extends GenericRepository<Cart, Integer> {

    @Query("SELECT * FROM \"carts\" WHERE \"id\" = :id")
    Optional<Cart> findById(Integer id);

    @Query("SELECT * FROM \"carts\" WHERE \"userId\" = :userId AND \"productId\" = :productId")
    List<Cart> findByUserAndProduct(int userId, int productId);

    @Query("SELECT * FROM \"carts\" WHERE \"userId\" = :userId ")
    List<Cart> findByUser(int userId);

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"carts\"(\"productId\", \"quantity\", \"userId\") VALUES(:productId, :quantity, :userId)")
    void create(int productId, int quantity, int userId);

    @TransactionalAdvice("default")
    @Transactional
    @Query("UPDATE \"carts\" SET \"productId\" = :productId, \"quantity\" = :quantity, \"userId\" = :userId WHERE \"id\" = :id")
    void update(int id, int productId, int quantity, int userId);

    @TransactionalAdvice("default")
    @Transactional
    @Query("DELETE FROM \"carts\" WHERE \"id\" = :id")
    void delete(int id);
}

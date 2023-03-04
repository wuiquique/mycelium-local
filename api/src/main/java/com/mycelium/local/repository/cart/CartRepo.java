package com.mycelium.local.repository.cart;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Join;
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

    @Query("SELECT c.*, p_.\"name\" AS p_name, p_.\"desc\" AS p_desc, p_.\"weight\" AS p_weight, p_.\"price\" AS p_price, j_.\"url\" AS j_url FROM \"carts\" c INNER JOIN \"products\" p_ ON c.\"productId\" = p_.\"id\" INNER JOIN \"pictures\" j_ ON j_.\"productId\" = p_.\"id\" WHERE \"userId\" = :userId ")
    @Join(value = "product", alias = "p_")
    @Join(value = "picture", alias = "j_")
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

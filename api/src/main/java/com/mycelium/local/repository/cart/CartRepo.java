package com.mycelium.local.repository.cart;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface CartRepo extends CrudRepository<Cart, Integer> {
    List<Cart> findByUserIdAndProductId(int userId, int productId);

    List<Cart> findByUserId(int userId);
}

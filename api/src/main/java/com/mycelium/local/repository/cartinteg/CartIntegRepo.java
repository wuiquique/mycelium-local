package com.mycelium.local.repository.cartinteg;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface CartIntegRepo extends CrudRepository<CartInteg, Integer> {
    List<CartInteg> findByUserIdAndProductId(int userId, int productId);

    List<CartInteg> findByUserId(int userId);
}

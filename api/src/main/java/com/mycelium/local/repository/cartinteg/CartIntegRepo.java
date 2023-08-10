package com.mycelium.local.repository.cartinteg;

import java.util.List;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface CartIntegRepo extends CrudRepository<CartInteg, Integer> {
    @Join(value = "integration", type = Join.Type.FETCH)
    List<CartInteg> findByUserIdAndProductId(int userId, String productId);

    @Join(value = "integration", type = Join.Type.FETCH)
    List<CartInteg> findByUserId(int userId);
}

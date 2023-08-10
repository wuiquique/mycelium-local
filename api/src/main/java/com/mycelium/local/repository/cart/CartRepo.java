package com.mycelium.local.repository.cart;

import java.util.List;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface CartRepo extends CrudRepository<Cart, Integer> {
    @Join(value = "product", type = Join.Type.FETCH)
    @Join(value = "product.categorie", type = Join.Type.FETCH)
    @Join(value = "product.pictures", type = Join.Type.LEFT_FETCH)
    List<Cart> findByUserIdAndProductId(int userId, int productId);

    @Join(value = "product", type = Join.Type.FETCH)
    @Join(value = "product.categorie", type = Join.Type.FETCH)
    @Join(value = "product.pictures", type = Join.Type.LEFT_FETCH)
    List<Cart> findByUserId(int userId);
}

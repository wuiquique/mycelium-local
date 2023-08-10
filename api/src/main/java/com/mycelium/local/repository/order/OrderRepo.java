package com.mycelium.local.repository.order;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface OrderRepo extends CrudRepository<Order, Integer> {
    @Join(value = "orderProducts", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.status", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.product", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.product.pictures", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.orderMessages", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.orderMessages.status", type = Join.Type.LEFT_FETCH)
    Optional<Order> findById(@NotNull Integer id);

    @Join(value = "orderProducts", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.status", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.product", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.product.pictures", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.orderMessages", type = Join.Type.LEFT_FETCH)
    @Join(value = "orderProducts.orderMessages.status", type = Join.Type.LEFT_FETCH)
    List<Order> findByUserId(@NotNull Integer user_id);
}

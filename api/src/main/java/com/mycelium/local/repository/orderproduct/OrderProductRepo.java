package com.mycelium.local.repository.orderproduct;

import java.util.List;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface OrderProductRepo extends CrudRepository<OrderProduct, Integer> {
    @Join(value = "product", type = Join.Type.FETCH)
    @Join(value = "product.pictures", type = Join.Type.LEFT_FETCH)
    List<OrderProduct> findByOrderId(int orderId);
}

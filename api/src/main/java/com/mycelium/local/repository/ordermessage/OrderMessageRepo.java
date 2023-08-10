package com.mycelium.local.repository.ordermessage;

import java.util.List;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface OrderMessageRepo extends CrudRepository<OrderMessage, Integer> {
    @Join(value = "status", type = Join.Type.FETCH)
    List<OrderMessage> findByOrderProductId(int orderId);

    @Join(value = "status", type = Join.Type.FETCH)
    List<OrderMessage> findByOrderProductIdAndStatusId(int orderId, int statusId);
}

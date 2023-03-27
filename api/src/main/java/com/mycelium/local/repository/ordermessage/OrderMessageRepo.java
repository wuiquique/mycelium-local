package com.mycelium.local.repository.ordermessage;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.annotation.Join;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface OrderMessageRepo extends CrudRepository<OrderMessage, Integer> {
    @Join(value = "status", type = Join.Type.FETCH)
    List<OrderMessage> findByOrderId(int orderId);
    List<OrderMessage> findByOrderIdAndStatusId(int orderId, int statusId);
}

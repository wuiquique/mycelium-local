package com.mycelium.local.repository.integorderproduct;

import java.util.List;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface IntegOrderProductRepo extends CrudRepository<IntegOrderProduct, Integer> {
    @Join(value = "integration", type = Join.Type.FETCH)
    @Join(value = "statusInteg", type = Join.Type.LEFT_FETCH)
    @Join(value = "statusLocal", type = Join.Type.LEFT_FETCH)
    List<IntegOrderProduct> findByOrderId(int orderId);
}

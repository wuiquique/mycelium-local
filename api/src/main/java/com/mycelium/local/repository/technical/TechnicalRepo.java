package com.mycelium.local.repository.technical;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository
public interface TechnicalRepo extends CrudRepository<Technical, Integer> {
    List<Technical> findByProductId(Integer productId);
}

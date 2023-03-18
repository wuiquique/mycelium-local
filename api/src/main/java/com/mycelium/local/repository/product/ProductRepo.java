package com.mycelium.local.repository.product;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface ProductRepo extends CrudRepository<Product, Integer> {
    List<Product> findByCategorieId(Integer categorieId);
}
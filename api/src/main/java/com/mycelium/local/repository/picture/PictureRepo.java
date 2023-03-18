package com.mycelium.local.repository.picture;

import java.util.List;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface PictureRepo extends CrudRepository<Picture, Integer> {
    @Query("SELECT * FROM \"pictures\" WHERE \"productId\" = :productId")
    List<Picture> findByProductId(Integer productId);
}

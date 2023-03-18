package com.mycelium.local.repository.rating;

import java.util.List;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface RatingRepo extends CrudRepository<Rating, Integer> {
    List<Rating> findByProductId(int productId);

    @Query("SELECT AVG(\"rating\") FROM \"ratings\" WHERE \"productId\" = :id")
    int findAvgByPId(Integer id);
}

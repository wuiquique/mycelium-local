package com.mycelium.local.repository.rating;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface RatingRepo extends GenericRepository<Rating, Integer> {

    @Query("SELECT * FROM \"ratings\" WHERE \"productId\" = :id")
    List<Rating> findByProductId(Integer id);

    @Query("SELECT * FROM \"ratings\"")
    List<Rating> findAll();

    @Query("SELECT AVG(\"rating\") FROM \"ratings\" WHERE \"productId\" = :id")
    int findAvgByPId(Integer id);

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"ratings\"(\"userId\", \"productId\", \"rating\") VALUES(:userId, :productId, :rating)")
    void create(int userId, int productId, int rating);

    @TransactionalAdvice("default")
    @Transactional
    @Query("UPDATE \"ratings\" SET \"rating\" = :rating WHERE \"userId\" = :userId")
    void update(int rating, int userId);
}

package com.mycelium.local.repository.rating;

import java.util.List;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface RatingRepo extends CrudRepository<Rating, Integer> {
    List<Rating> findByProductId(int productId);

    @Query("SELECT AVG(RATING) FROM RATING WHERE PRODUCTID = :id")
    int findAvgByPId(Integer id);

    @Query("UPDATE RATING SET RATING = :rating WHERE USERID = :userId")
    void updateByUserId(int rating, int userId);
}

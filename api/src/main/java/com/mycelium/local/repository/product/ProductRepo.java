package com.mycelium.local.repository.product;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface ProductRepo extends CrudRepository<Product, Integer> {
    @Join(value = "pictures", type = Join.Type.LEFT_FETCH)
    @Join(value = "technical", type = Join.Type.LEFT_FETCH)
    @Join(value = "categorie", type = Join.Type.LEFT_FETCH)
    Iterable<Product> findAll();

    @Join(value = "pictures", type = Join.Type.LEFT_FETCH)
    @Join(value = "technical", type = Join.Type.LEFT_FETCH)
    @Join(value = "categorie", type = Join.Type.LEFT_FETCH)
    Optional<Product> findById(@NotNull Integer id);

    @Join(value = "pictures", type = Join.Type.LEFT_FETCH)
    @Join(value = "technical", type = Join.Type.LEFT_FETCH)
    @Join(value = "categorie", type = Join.Type.LEFT_FETCH)
    List<Product> findByCategorieId(Integer categorieId);

    @Join(value = "pictures", type = Join.Type.LEFT_FETCH)
    @Join(value = "technical", type = Join.Type.LEFT_FETCH)
    @Join(value = "categorie", type = Join.Type.LEFT_FETCH)
    List<Product> findByIdInList(List<Integer> ids);

    @Query("SELECT * FROM (SELECT p.id, sum(o.QUANTITY) AS suma FROM ORDERPRODUCT o JOIN PRODUCT p ON o.PRODUCTID = p.ID GROUP BY p.ID ORDER BY suma DESC) a JOIN PRODUCT p2 ON a.id = p2.ID")
    List<Product> findTop3Sales();

    @Query("SELECT p.ID , p.name, p.\"DESC\" , p.BRAND , p.WEIGHT , p.QUANTITY , p.PRICE , p.CATEGORIEID FROM (SELECT o.PRODUCTID, max(o.CREATED) AS created FROM ORDERPRODUCT o GROUP BY o.PRODUCTID ORDER BY max(o.CREATED) DESC) a JOIN PRODUCT p ON p.id = a.productid")
    List<Product> findLastBought();
}
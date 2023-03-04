package com.mycelium.local.repository.product;

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
public interface ProductRepo extends GenericRepository<Product, Integer> {

    @Query("SELECT * FROM \"products\" WHERE id = :id")
    Optional<Product> findById(Integer id);

    @Query("SELECT * FROM \"products\"")
    List<Product> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"products\"(\"name\", \"desc\", \"categorieId\", \"brand\", \"weight\", \"quantity\", \"price\") VALUES(:name, :desc, :categorieId, :brand, :weight, :quantity, :price)")
    void create(String name, String desc, int categorieId, String brand, int weight, int quantity, int price);

}
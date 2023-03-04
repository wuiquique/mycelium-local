package com.mycelium.local.repository.cartinteg;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface CartIntegRepo extends GenericRepository<CartInteg, Integer> {

    @Query("SELECT * FROM \"cartInteg\" WHERE id = :id")
    Optional<CartInteg> findById(Integer id);

    @Query("SELECT * FROM \"cartInteg\"")
    List<CartInteg> findAll();

    @Transactional
    @Query("INSERT INTO \"cartInteg\"(\"productId\", \"quantity\", \"userId\") VALUES(:productId, :quantity, :userId)")
    void create(int productId, int quantity, int userId);

}

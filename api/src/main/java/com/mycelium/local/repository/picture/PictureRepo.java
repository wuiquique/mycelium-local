package com.mycelium.local.repository.picture;

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
public interface PictureRepo extends GenericRepository<Picture, Integer> {

    @Query("SELECT * FROM \"pictures\" WHERE id = :id")
    Optional<Picture> findById(Integer id);

    @Query("SELECT * FROM \"pictures\"")
    List<Picture> findAll();

    @Query("SELECT * FROM \"pictures\" WHERE \"productId\" = :productId")
    List<Picture> findByProductId(Integer productId);

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"pictures\"(\"url\", \"productId\") VALUES(:url, :productId)")
    void create(String url, int productId);
}

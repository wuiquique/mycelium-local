package com.mycelium.local.repository.categorie;

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
public interface CategorieRepo extends GenericRepository<Categorie, Integer> {

    @Query("SELECT * FROM \"categories\" WHERE id = :id")
    Optional<Categorie> findById(Integer id);

    @Query("SELECT * FROM \"categories\"")
    List<Categorie> findAll();

    @Transactional
    @Query("INSERT INTO \"categories\"(\"name\") VALUES(:name)")
    void create(String name);

}
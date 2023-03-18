package com.mycelium.local.repository.categorie;

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
public interface CategorieRepo extends GenericRepository<Categorie, Integer> {

    @Query("SELECT * FROM \"categories\" WHERE id = :id")
    Optional<Categorie> findById(Integer id);

    @Query("SELECT * FROM \"categories\"")
    List<Categorie> findAll();

    @Query("SELECT * FROM \"categories\" WHERE \"name\" = :name LIMIT 1")
    Optional<Categorie> findByName(String name);

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"categories\"(\"name\") VALUES(:name) RETURNING \"id\"")
    long create(String name);

    @TransactionalAdvice("default")
    @Transactional
    @Query("Update \"categories\" SET \"name\" = :name WHERE \"id\" = :id")
    void update(int id, String name);

    @TransactionalAdvice("default")
    @Transactional
    @Query("DELETE FROM \"categories\" WHERE \"id\" = :id")
    void delete(int id);
}
package com.mycelium.local.repository.categorie;

import java.util.Optional;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface CategorieRepo extends CrudRepository<Categorie, Integer> {
    Optional<Categorie> findByName(String name);
}
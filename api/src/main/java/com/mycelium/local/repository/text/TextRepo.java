package com.mycelium.local.repository.text;

import java.util.Optional;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface TextRepo extends CrudRepository<TranslationText, Long> {
    public Optional<TranslationText> findByComponentAndKey(String component, String key);
}
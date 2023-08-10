package com.mycelium.local.repository.text;

import java.util.Optional;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface TextRepo extends CrudRepository<TranslationText, Long> {
    public Optional<TranslationText> findByComponentAndKey(String component, String key);
}

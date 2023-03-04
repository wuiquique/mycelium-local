package com.mycelium.local.repository.user;

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
public interface UserRepo extends GenericRepository<User, Integer> {

    @Query("Select * FROM \"users\" INNER JOIN \"userRole\" ON \"users\".\"roleId\" = \"userRole\".\"id\" WHERE \"users\".\"id\" = :id")
    Optional<User> findById(Integer id);

    @Query("SELECT * FROM \"users\"")
    List<User> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("UPDATE \"users\" SET \"name\" = :name, \"lastname\" = :lastname, \"email\" = :email, \"roleId\" = :roleId WHERE ID = :id")
    void update(Integer id, String name, String lastname, String email, Integer roleId);
}
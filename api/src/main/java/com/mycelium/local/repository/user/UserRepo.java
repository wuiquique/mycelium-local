package com.mycelium.local.repository.user;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface UserRepo extends GenericRepository<User, Integer> {

    @Query("SELECT u.*, r_.\"name\" AS r_name FROM \"users\" u INNER JOIN \"userRole\" r_ ON u.\"roleId\" = r_.\"id\" WHERE u.\"id\" = :id")
    @Join(value = "role", alias = "r_")
    Optional<User> findById(Integer id);

    @Query("SELECT u.*, r_.\"name\" AS r_name FROM \"users\" u INNER JOIN \"userRole\" r_ ON u.\"roleId\" = r_.\"id\"")
    @Join(value = "role", alias = "r_")
    List<User> findAll();

    @Query("SELECT * FROM \"users\" WHERE \"email\" = :email")
    List<User> findByEmail(String email);

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"users\"(\"name\", \"lastname\", \"email\", \"password\", \"roleId\") VALUES(:name, :lastname, :email, :password, :roleId)")
    void create(String name, String lastname, String email, String password, Integer roleId);

    @TransactionalAdvice("default")
    @Transactional
    @Query("UPDATE \"users\" SET \"name\" = :name, \"lastname\" = :lastname, \"email\" = :email, \"roleId\" = :roleId WHERE \"id\" = :id")
    void update(Integer id, String name, String lastname, String email, Integer roleId);
}
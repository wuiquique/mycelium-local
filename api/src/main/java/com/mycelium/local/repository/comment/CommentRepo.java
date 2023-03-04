package com.mycelium.local.repository.comment;

import java.util.Date;
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
public interface CommentRepo extends GenericRepository<Comment, Integer> {

    @Query("SELECT * FROM \"comments\" WHERE id = :id")
    Optional<Comment> findById(Integer id);

    @Query("SELECT * FROM \"comments\"")
    List<Comment> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"comments\"(\"userId\", \"productId\", \"commentId\", \"message\", \"created\", \"updated\") VALUES(:userId, :productId, :commentId, :message, :created, :updated)")
    void create(int userId, int productId, int commentId, String message, Date created, Date updated);

}

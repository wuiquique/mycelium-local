package com.mycelium.local.repository.comment;

import java.util.List;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository("default")
@JdbcRepository()
public interface CommentRepo extends CrudRepository<Comment, Integer> {
    @Query("SELECT c_.*, u_.ID AS U_ID, u_.NAME AS U_NAME, u_.LASTNAME AS U_LASTNAME, u_.EMAIL AS U_EMAIL, u_.PASSWORD AS U_PASSWORD, u_.ROLEID AS U_ROLEID FROM \"COMMENT\" c_ JOIN \"USER\" u_ ON c_.USERID = u_ .ID WHERE c_.PRODUCTID = :productId AND (c_.COMMENTID = :commentId OR (:commentId IS NULL AND c_.COMMENTID IS NULL))")
    @Join(value = "user", type = Join.Type.FETCH, alias = "u_")
    List<Comment> findByProductIdAndCommentId(int productId, @Nullable Integer commentId);
}

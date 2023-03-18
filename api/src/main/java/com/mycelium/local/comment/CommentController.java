package com.mycelium.local.comment;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.comment.Comment;
import com.mycelium.local.repository.comment.CommentRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class CommentCreateRequest {
    public int userId;
    public int productId;
    public int commentId;
    public String message;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/comment")
public class CommentController {

    private CommentRepo commentRepo;

    public CommentController(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Get("/")
    public List<Comment> list() {
        return Lists.newArrayList(commentRepo.findAll());
    }

    @Get("/{id}")
    public Comment get(int id) {
        return commentRepo.findById(id).get();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body CommentCreateRequest body) {
        var newComment = new Comment();
        newComment.user.id = body.userId;
        newComment.product.id = body.productId;
        newComment.comment.id = body.commentId;
        newComment.message = body.message;
        newComment.created = new Date();
        newComment.updated = new Date();
        commentRepo.save(newComment);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void update() {
        // TODO
    }
}
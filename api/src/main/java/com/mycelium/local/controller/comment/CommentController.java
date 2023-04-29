package com.mycelium.local.controller.comment;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.mycelium.local.repository.comment.Comment;
import com.mycelium.local.repository.comment.CommentRepo;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

/**
 * representa un request para crear un nuevo comentario
 */
class CommentCreateRequest {
    public int commentId;
    public String message;
}

/**
 * Representa un arbol de comentarios, los cuales incluyen informacion sobre los padres y sus hijos
 */
@Introspected
@JsonInclude(JsonInclude.Include.ALWAYS)
class CommentTree {
    public Integer id;
    public String message;
    public Date created;
    public Date updated;
    public int productId;
    public String userName;
    public List<CommentTree> children;

    public CommentTree(Integer id, String message, Date created, Date updated, int productId, String userName,
            List<CommentTree> children) {
        this.id = id;
        this.message = message;
        this.created = created;
        this.updated = updated;
        this.productId = productId;
        this.userName = userName;
        this.children = children;
    }
}

/**
 * Clase de controlador para manejar comentarios
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/comment")
public class CommentController {

    private CommentRepo commentRepo;
    private UserRepo userRepo;
    private ProductRepo productRepo;

    public CommentController(CommentRepo commentRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

     /**
     * CRea un arbol de comentarios para un ID de producto dado y un ID de comentario opcional
     *
     * @param productId El ID del producto sobre el que se formara el arbol
     * @param commentId El ID del comentario sobre el que se formara el arbol (Opcional, default null)
     * @return Una lista de objetos CommentTree que representan el arbol de comentarios
     */
    public List<CommentTree> buildCommentTree(int productId, Integer commentId) {
        List<CommentTree> res = Lists.newArrayList();
        for (var comment : commentRepo.findByProductIdAndCommentId(productId, commentId)) {
            res.add(new CommentTree(comment.id, comment.message, comment.created, comment.updated, productId,
                    comment.user.name, buildCommentTree(productId, comment.id)));
        }
        return res;
    }

    /**
     * Lista de comentarios para un ID de producto en especifico.
     *
     * @param productId El ID de la lista de comentarios
     * @return Una lista de objetos CommentTree que representan el arbol de comentarios
     */
    @Get("/{productId}")
    public List<CommentTree> list(int productId) {
        return buildCommentTree(productId, null);
    }

     /**
     * Crea un nuevo producto para el ID del producto dado
     *
     * @param authentication un objeto que representa la informacion de autentificacion de un usuario
     * @param productId El ID del producto al cual se esta haciendo el comentario
     * @param body un objeto CommentCreateRequest que contiene informacion del nuevo comentario
     */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/{productId}")
    public void create(Authentication authentication, int productId, @Body CommentCreateRequest body) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        var newComment = new Comment();
        newComment.user = userRepo.findById(userId).get();
        newComment.product = productRepo.findById(productId).get();
        newComment.comment = commentRepo.findById(body.commentId).orElse(null);
        newComment.message = body.message;
        newComment.created = new Date();
        newComment.updated = new Date();
        commentRepo.save(newComment);
    }
}
package com.mycelium.local.repository.comment;

import java.util.Date;

import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.user.User;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class Comment {
    @Id
    @GeneratedValue
    public Integer id;
    public String message;
    @DateCreated
    public Date created;
    @DateUpdated
    public Date updated;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Product product;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public User user;

    @Nullable
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Comment comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}

package com.mycelium.local.repository.cart;

import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.user.User;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class Cart {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    public Integer id;
    public int quantity;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public User user;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

}

package com.mycelium.local.repository.integration;

import java.util.List;

import com.mycelium.local.repository.cartinteg.CartInteg;
import com.mycelium.local.repository.integorderproduct.IntegOrderProduct;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class Integration {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    public Integer id;
    public String name;
    public String request;
    public String user;
    public String password;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "integration")
    public List<CartInteg> cartInteg;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "integration")
    public List<IntegOrderProduct> integOrderProducts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CartInteg> getCartInteg() {
        return cartInteg;
    }

    public void setCartInteg(List<CartInteg> cartInteg) {
        this.cartInteg = cartInteg;
    }

    public List<IntegOrderProduct> getIntegOrderProducts() {
        return integOrderProducts;
    }

    public void setIntegOrderProducts(List<IntegOrderProduct> integOrderProducts) {
        this.integOrderProducts = integOrderProducts;
    }
}

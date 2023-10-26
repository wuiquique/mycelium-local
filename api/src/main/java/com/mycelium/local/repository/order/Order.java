package com.mycelium.local.repository.order;

import java.sql.Timestamp;
import java.util.List;

import com.mycelium.local.repository.orderproduct.OrderProduct;
import com.mycelium.local.repository.user.User;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class Order {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    public Integer id;
    public String direction;
    public String state;
    public String city;
    public int zip;
    public int phone;
    public Timestamp since;
    public Timestamp till;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public User user;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "order")
    public List<OrderProduct> orderProducts;

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Timestamp getSince() {
        return since;
    }

    public void setSince(Timestamp since) {
        this.since = since;
    }

    public Timestamp getTill() {
        return till;
    }

    public void setTill(Timestamp till) {
        this.till = till;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

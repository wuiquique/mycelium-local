package com.mycelium.local.repository.ordermessage;

import com.mycelium.local.repository.orderproduct.OrderProduct;
import com.mycelium.local.repository.status.Status;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class OrderMessage {
    @Id
    @GeneratedValue
    public Integer id;
    public String name;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public OrderProduct orderProduct;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Status status;

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

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}

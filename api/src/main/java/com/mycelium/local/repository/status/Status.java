package com.mycelium.local.repository.status;

import com.mycelium.local.repository.ordermessage.OrderMessage;

import java.util.List;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class Status {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    public Integer id;
    public String name;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "status")
    public List<OrderMessage> orderMessages;

    public List<OrderMessage> getOrderMessages() {
        return orderMessages;
    }

    public void setOrderMessage(List<OrderMessage> orderMessages) {
        this.orderMessages = orderMessages;
    }

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

}

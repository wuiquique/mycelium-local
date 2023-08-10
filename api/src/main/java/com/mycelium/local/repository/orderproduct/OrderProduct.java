package com.mycelium.local.repository.orderproduct;

import java.util.Date;
import java.util.List;

import com.mycelium.local.repository.order.Order;
import com.mycelium.local.repository.ordermessage.OrderMessage;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.status.Status;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class OrderProduct {
    @Id
    @GeneratedValue
    public Integer id;
    public int quantity;
    public String tracking;
    public int time;
    @DateCreated
    public Date created;
    @DateUpdated
    public Date updated;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Product product;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Order order;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Status status;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "orderProduct")
    public List<OrderMessage> orderMessages;

    public List<OrderMessage> getOrderMessages() {
        return orderMessages;
    }

    public void setOrderMessages(List<OrderMessage> orderMessages) {
        this.orderMessages = orderMessages;
    }

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

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

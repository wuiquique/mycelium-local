package com.mycelium.local.repository.integorderproduct;

import java.util.Date;

import com.mycelium.local.repository.integration.Integration;
import com.mycelium.local.repository.order.Order;
import com.mycelium.local.repository.status.Status;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.Raw.class)
public class IntegOrderProduct {
    @Id
    @GeneratedValue
    public Integer id;
    public int productId;
    public int quantity;
    public String trackingInteg;
    public String trackingLocal;
    public int timeInteg;
    public int timeLocal;
    @DateCreated
    public Date created;
    @DateUpdated
    public Date updated;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Order order;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Status statusInteg;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Status statusLocal;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Integration integration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTrackingInteg() {
        return trackingInteg;
    }

    public void setTrackingInteg(String trackingInteg) {
        this.trackingInteg = trackingInteg;
    }

    public String getTrackingLocal() {
        return trackingLocal;
    }

    public void setTrackingLocal(String trackingLocal) {
        this.trackingLocal = trackingLocal;
    }

    public int getTimeInteg() {
        return timeInteg;
    }

    public void setTimeInteg(int timeInteg) {
        this.timeInteg = timeInteg;
    }

    public int getTimeLocal() {
        return timeLocal;
    }

    public void setTimeLocal(int timeLocal) {
        this.timeLocal = timeLocal;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Status getStatusInteg() {
        return statusInteg;
    }

    public void setStatusInteg(Status statusInteg) {
        this.statusInteg = statusInteg;
    }

    public Status getStatusLocal() {
        return statusLocal;
    }

    public void setStatusLocal(Status statusLocal) {
        this.statusLocal = statusLocal;
    }

    public Integration getIntegration() {
        return integration;
    }

    public void setIntegration(Integration integration) {
        this.integration = integration;
    }

}

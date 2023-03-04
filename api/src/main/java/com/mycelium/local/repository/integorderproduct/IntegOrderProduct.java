package com.mycelium.local.repository.integorderproduct;

import java.util.Date;

import com.mycelium.local.repository.product.Product;

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
    public int orderId;
    public int productId;
    public int quantity;
    public int statusIntegId;
    public int statusLocalId;
    public String trackingInteg;
    public String trackingLocal;
    public int timeInteg;
    public int timeLocal;
    public int integOrderId;
    @DateCreated
    public Date created;
    @DateUpdated
    public Date updated;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getStatusIntegId() {
        return statusIntegId;
    }

    public void setStatusIntegId(int statusIntegId) {
        this.statusIntegId = statusIntegId;
    }

    public int getStatusLocalId() {
        return statusLocalId;
    }

    public void setStatusLocalId(int statusLocalId) {
        this.statusLocalId = statusLocalId;
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

    public int getIntegOrderId() {
        return integOrderId;
    }

    public void setIntegOrderId(int integOrderId) {
        this.integOrderId = integOrderId;
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

}

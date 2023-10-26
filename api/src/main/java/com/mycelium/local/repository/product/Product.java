package com.mycelium.local.repository.product;

import java.util.List;

import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.orderproduct.OrderProduct;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.technical.Technical;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class Product {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    public Integer id;
    public String name;
    public String desc;
    public String brand;
    public int weight;
    public int quantity;
    public int price;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public Categorie categorie;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "product")
    public List<Picture> pictures;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "product")
    public List<Technical> technical;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "product")
    public List<OrderProduct> orderProducts;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "product")
    public List<Cart> cartProducts;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Technical> getTechnical() {
        return technical;
    }

    public void setTechnical(List<Technical> technical) {
        this.technical = technical;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}

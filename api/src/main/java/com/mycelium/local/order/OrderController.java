package com.mycelium.local.order;

import java.util.List;
import java.util.Date;

import com.mycelium.local.repository.order.Order;
import com.mycelium.local.repository.order.OrderRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.annotation.Secured;

class OrderCreateRequest {
    public int userId;
    public String direction;
    public String state;
    public String city;
    public int zip;
    public int phone;
    public Date since;
    public Date till;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/user/order")
public class OrderController {

    private OrderRepo orderRepo;

    public OrderController(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Get("/")
    public List<Order> list() {
        return orderRepo.findAll();
    }

    @Get("/{id}")
    public Order get(int id) {
        return orderRepo.findById(id).get();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body OrderCreateRequest body) {
        orderRepo.create(body.userId, body.direction, body.state, body.city, body.zip, body.phone, body.since,
                body.till);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void update() {
        // TODO
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/")
    public void delete() {
        // TODO
    }
}
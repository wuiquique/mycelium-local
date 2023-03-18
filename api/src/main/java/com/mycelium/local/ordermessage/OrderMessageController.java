package com.mycelium.local.ordermessage;

import java.util.List;

import com.mycelium.local.repository.ordermessage.OrderMessage;
import com.mycelium.local.repository.ordermessage.OrderMessageRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class OrderMessageCreateRequest {
    public int orderId;
    public int statusId;
    public String name;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/user/order/{orderID}/message/{statusId}")
public class OrderMessageController {

    private OrderMessageRepo orderMessageRepo;

    public OrderMessageController(OrderMessageRepo orderMessageRepo) {
        this.orderMessageRepo = orderMessageRepo;
    }

    @Get("/")
    public List<OrderMessage> list() {
        return orderMessageRepo.findAll();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body OrderMessageCreateRequest body) {
        orderMessageRepo.create(body.orderId, body.statusId, body.name);
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
package com.mycelium.local.controller.ordermessage;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.order.OrderRepo;
import com.mycelium.local.repository.ordermessage.OrderMessage;
import com.mycelium.local.repository.ordermessage.OrderMessageRepo;
import com.mycelium.local.repository.status.StatusRepo;
import com.mycelium.local.repository.orderproduct.OrderProduct; 
import com.mycelium.local.repository.orderproduct.OrderProductRepo;

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
    private OrderRepo orderRepo;
    private StatusRepo statusRepo;

    public OrderMessageController(OrderMessageRepo orderMessageRepo, OrderRepo orderRepo, StatusRepo statusRepo) {
        this.orderMessageRepo = orderMessageRepo;
        this.orderRepo = orderRepo;
        this.statusRepo = statusRepo;
    }

    @Get("/")
    public List<OrderMessage> list() {
        return Lists.newArrayList(orderMessageRepo.findAll());
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public OrderMessage create(@Body OrderMessageCreateRequest body) {
        var newOrderMessage = new OrderMessage();
        newOrderMessage.order = orderRepo.findById(body.orderId).get();
        newOrderMessage.status = statusRepo.findById(body.statusId).get();
        newOrderMessage.name = body.name;
        orderMessageRepo.save(newOrderMessage);
        return newOrderMessage;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public OrderMessage update(@Body OrderMessageCreateRequest body) {
        var existing = orderMessageRepo.findByOrderIdAndStatusId(body.orderId, body.statusId);
        for (var item : existing) {
            orderMessageRepo.delete(item);
        }
        var newOrderMessage = new OrderMessage();
        newOrderMessage.order = orderRepo.findById(body.orderId).get();
        newOrderMessage.status = statusRepo.findById(body.statusId).get();
        newOrderMessage.name = body.name;
        orderMessageRepo.save(newOrderMessage);
        return newOrderMessage;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/")
    public void delete() {
        // TODO
    }
}
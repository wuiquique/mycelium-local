package com.mycelium.local.controller.ordermessage;

import java.util.Map;
import java.util.Optional;

import com.mycelium.local.repository.ordermessage.OrderMessage;
import com.mycelium.local.repository.ordermessage.OrderMessageRepo;
import com.mycelium.local.repository.orderproduct.OrderProductRepo;
import com.mycelium.local.repository.status.StatusRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class OrderMessageCreateRequest {
    public boolean international;
    public String name;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/user/orderproduct/{orderProductId}/message/{statusId}")
public class OrderMessageController {

    private OrderMessageRepo orderMessageRepo;
    private OrderProductRepo orderProductRepo;
    private StatusRepo statusRepo;

    public OrderMessageController(OrderMessageRepo orderMessageRepo, OrderProductRepo orderProductRepo,
            StatusRepo statusRepo) {
        this.orderMessageRepo = orderMessageRepo;
        this.orderProductRepo = orderProductRepo;
        this.statusRepo = statusRepo;
    }

    @Get("/")
    public Object get(int orderProductId, int statusId) {
        var existing = orderMessageRepo.findByOrderProductIdAndStatusId(orderProductId, statusId);
        return existing.size() > 0 ? existing.get(0) : Map.of();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(int orderProductId, int statusId, @Body OrderMessageCreateRequest body) {
        var newOrderMessage = new OrderMessage();
        newOrderMessage.orderProduct = orderProductRepo.findById(orderProductId).get();
        newOrderMessage.status = statusRepo.findById(statusId).get();
        newOrderMessage.name = body.name;
        orderMessageRepo.save(newOrderMessage);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void update(int orderProductId, int statusId, @Body OrderMessageCreateRequest body) {
        var existing = orderMessageRepo.findByOrderProductIdAndStatusId(orderProductId, statusId);
        Optional<OrderMessage> opt = existing.size() > 0 ? Optional.of(existing.get(0)) : Optional.empty();

        var orderMessage = opt.get();

        orderMessage.orderProduct = orderProductRepo.findById(orderProductId).get();
        orderMessage.status = statusRepo.findById(statusId).get();
        orderMessage.name = body.name;
        orderMessageRepo.update(orderMessage);
    }
}
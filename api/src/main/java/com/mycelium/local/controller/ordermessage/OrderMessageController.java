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

/**
    Clase que define la estructura de una solicitud de creación de mensaje de orden.
    */
class OrderMessageCreateRequest {
    public boolean international;
    public String name;
}

/**
    Controlador encargado de manejar las solicitudes relacionadas con los mensajes de orden.
    */
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

    /**
    Obtiene el mensaje de orden que coincide con los IDs de la orden de producto y estado especificados.
    @param orderProductId El ID de la orden de producto.
    @param statusId El ID del estado del mensaje de orden.
    @return Un objeto existente que representa el mensaje de orden si existe, o un mapa vacío si no existe.
    */
    @Get("/")
    public Object get(int orderProductId, int statusId) {
        var existing = orderMessageRepo.findByOrderProductIdAndStatusId(orderProductId, statusId);
        return existing.size() > 0 ? existing.get(0) : Map.of();
    }

    /**
    Crea un nuevo mensaje de orden.
    @param orderProductId El ID de la orden de producto.
    @param statusId El ID del estado del mensaje de orden.
    @param body Un objeto que contiene los datos necesarios para crear el mensaje de orden.
    */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(int orderProductId, int statusId, @Body OrderMessageCreateRequest body) {
        var newOrderMessage = new OrderMessage();
        newOrderMessage.orderProduct = orderProductRepo.findById(orderProductId).get();
        newOrderMessage.status = statusRepo.findById(statusId).get();
        newOrderMessage.name = body.name;
        orderMessageRepo.save(newOrderMessage);
    }

    /**
    Actualiza un mensaje de orden existente.
    @param orderProductId El ID de la orden de producto.
    @param statusId El ID del estado del mensaje de orden.
    @param body Un objeto que contiene los nuevos datos para actualizar el mensaje de orden.
    */
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
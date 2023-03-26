package com.mycelium.local.controller.order;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.cart.CartRepo;
import com.mycelium.local.repository.cartinteg.CartInteg;
import com.mycelium.local.repository.cartinteg.CartIntegRepo;
import com.mycelium.local.repository.integorderproduct.IntegOrderProduct;
import com.mycelium.local.repository.integorderproduct.IntegOrderProductRepo;
import com.mycelium.local.repository.order.Order;
import com.mycelium.local.repository.order.OrderRepo;
import com.mycelium.local.repository.orderproduct.OrderProduct;
import com.mycelium.local.repository.orderproduct.OrderProductRepo;
import com.mycelium.local.repository.status.StatusRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

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

@Introspected
@JsonInclude(Include.ALWAYS)
class OrderProductUni {

    public Integer orderId;
    public Integer productId;
    public String productName;
    public String productDesc;
    public Integer productCategorie;
    public String productBrand;
    public Integer productPrice;
    public Integer quantity;
    public Integer statusId;
    public Integer statusIntegId;
    public String tracking;
    public String trackingInteg;
    public Integer time;
    public Integer integOrderId;

    public OrderProductUni(Integer orderId, Integer productId, String productName, String productDesc,
            Integer productCategorie, String productBrand, Integer productPrice, Integer quantity,
            Integer statusId, Integer statusIntegId, String tracking, String trackingInteg,
            Integer time, Integer integOrderId) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productCategorie = productCategorie;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.statusId = statusId;
        this.statusIntegId = statusIntegId;
        this.tracking = tracking;
        this.trackingInteg = trackingInteg;
        this.time = time;
        this.integOrderId = integOrderId;
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class OrderFinalResponse {
    public String direction;
    public String state;
    public String city;
    public int zip;
    public int phone;
    public Date since;
    public Date till;
    public List<OrderProductUni> productList;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/user/order")
public class OrderController {

    private OrderRepo orderRepo;
    private OrderProductRepo orderProductRepo;
    private CartRepo cartRepo;
    private CartIntegRepo cartIntegRepo;
    private IntegOrderProductRepo integOrderProductRepo;
    private StatusRepo statusRepo;

    public OrderController(OrderRepo orderRepo, OrderProductRepo orderProductRepo,
            CartRepo cartRepo, CartIntegRepo cartIntegRepo, IntegOrderProductRepo integOrderProductRepo,
            StatusRepo statusRepo) {
        this.orderRepo = orderRepo;
        this.orderProductRepo = orderProductRepo;
        this.cartRepo = cartRepo;
        this.cartIntegRepo = cartIntegRepo;
        this.integOrderProductRepo = integOrderProductRepo;
        this.statusRepo = statusRepo;
    }

    @Get("/")
    public List<Order> list() {
        return Lists.newArrayList(orderRepo.findAll());
    }

    @Get("/{id}")
    public OrderFinalResponse get(int id) {
        var order = orderRepo.findById(id).get();
        var orderFinal = new OrderFinalResponse();
        orderFinal.direction = order.direction;
        orderFinal.state = order.state;
        orderFinal.city = order.city;
        orderFinal.zip = order.zip;
        orderFinal.phone = order.phone;
        orderFinal.since = order.since;
        orderFinal.till = order.till;

        List<OrderProductUni> products = new ArrayList<OrderProductUni>();

        for (OrderProduct orderP : orderProductRepo.findByOrderId(id)) {
            products.add(new OrderProductUni(orderP.id, orderP.product.id, orderP.product.name,
                    orderP.product.desc, orderP.product.categorie.id, orderP.product.brand,
                    orderP.product.price, orderP.quantity, orderP.status.id, null,
                    orderP.tracking, null, orderP.time, null));
        }
        for (IntegOrderProduct integOrderP : integOrderProductRepo.findByOrderId(id)) {
            products.add(new OrderProductUni(null, 1000, "Ejemplo Internacional", "Ejemplo Internacional",
                    3000, "Marca Internacional", 100, integOrderP.quantity, integOrderP.statusLocal.id, 1,
                    "Tracking Local", "Tracking Internacional", integOrderP.timeInteg + integOrderP.timeLocal,
                    integOrderP.id));
        }

        orderFinal.productList = products;

        return orderFinal;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body OrderCreateRequest body, Authentication auth) {
        var userMap = auth.getAttributes();
        var userId = (int) (long) userMap.get("id");

        var newOrder = new Order();
        newOrder.user.id = body.userId;
        newOrder.direction = body.direction;
        newOrder.state = body.state;
        newOrder.city = body.city;
        newOrder.zip = body.zip;
        newOrder.phone = body.phone;
        newOrder.since = body.since;
        newOrder.till = body.till;
        orderRepo.save(newOrder);

        for (Cart cart : cartRepo.findByUserId(userId)) {
            var newOrderProduct = new OrderProduct();
            newOrderProduct.order = newOrder;
            newOrderProduct.product = cart.product;
            newOrderProduct.quantity = cart.quantity;
            newOrderProduct.status = statusRepo.findById(1).get();
            newOrderProduct.tracking = trackingNumberString();
            newOrderProduct.time = 5;

            orderProductRepo.save(newOrderProduct);
        }

        for (CartInteg cart : cartIntegRepo.findByUserId(userId)) {
            var newIntegOrderProduct = new IntegOrderProduct();
            newIntegOrderProduct.order = newOrder;
            newIntegOrderProduct.productId = 1;
            newIntegOrderProduct.quantity = cart.quantity;
            newIntegOrderProduct.statusInteg = statusRepo.findById(1).get();
            newIntegOrderProduct.statusLocal = statusRepo.findById(1).get();
            newIntegOrderProduct.trackingInteg = "Tracking Internacional";
            newIntegOrderProduct.trackingLocal = "Tracking Local";
            newIntegOrderProduct.timeInteg = 10;
            newIntegOrderProduct.timeLocal = newIntegOrderProduct.timeInteg + 5;

            integOrderProductRepo.save(newIntegOrderProduct);
        }

        var existing = cartIntegRepo.findByUserId(userId);
        for (var cart : existing) {
            cartIntegRepo.delete(cart);
        }
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

    public String trackingNumberString() {
        byte[] array = new byte[10]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        return generatedString;
    }
}

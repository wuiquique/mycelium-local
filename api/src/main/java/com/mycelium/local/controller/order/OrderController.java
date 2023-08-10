package com.mycelium.local.controller.order;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.cart.CartRepo;
import com.mycelium.local.repository.cartinteg.CartInteg;
import com.mycelium.local.repository.cartinteg.CartIntegRepo;
import com.mycelium.local.repository.integorderproduct.IntegOrderProduct;
import com.mycelium.local.repository.integorderproduct.IntegOrderProductRepo;
import com.mycelium.local.repository.integration.Integration;
import com.mycelium.local.repository.integration.IntegrationRepo;
import com.mycelium.local.repository.order.Order;
import com.mycelium.local.repository.order.OrderRepo;
import com.mycelium.local.repository.orderproduct.OrderProduct;
import com.mycelium.local.repository.orderproduct.OrderProductRepo;
import com.mycelium.local.repository.status.Status;
import com.mycelium.local.repository.status.StatusRepo;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

class OrderCreateRequest {
    public String direction;
    public String state;
    public String city;
    public int zip;
    public int phone;
    public Timestamp since;
    public Timestamp till;
}

class OrderEstimado {
    public String productName;
    public Integer userId;
    public String address;
    public Integer categoryId;
    public Double boughtPrice;
    public Double porcentage;
    public Integer quantity;
    public Double weight;
    public Boolean international;
}

class OrderFactura {
    public String name;
    public String username;
    public String address;
    public Integer categoryId;
    public Double boughtPrice;
    public Double porcentage;
    public Integer quantity;
    public Double weight;
    public Boolean international;
}

class OrderImpuestos {
    public String name;
    public Object categoryId;
    public Double boughtPrice;
    public Double porcentage;
    public Integer quantity;
    public Double weight;
    public Boolean international;
}

class OrderImpuestos2 {
    public String name;
    public Integer categoryId;
    public String email;
    public String password;
    public Double boughtPrice;
    public Double porcentage;
    public Integer quantity;
    public Double weight;
    public Boolean international;
}

class BasicOrderMessage {
    public Integer statusId;
    public String name;

    public BasicOrderMessage(Integer statusId, String name) {
        this.statusId = statusId;
        this.name = name;
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class OrderProductUni {

    public Integer orderProductId;
    public Object productId;
    public String productName;
    public String productDesc;
    public Object productCategorie;
    public String productBrand;
    public Integer productPrice;
    public Integer quantity;
    public Integer weight;
    public Status status;
    public Status statusInteg;
    public String tracking;
    public String trackingInteg;
    public Integer time;
    public Integer integOrderId;
    public List<String> pictures;
    public List<BasicOrderMessage> messages;

    public OrderProductUni(Integer orderProductId, Object productId, String productName, String productDesc,
            Object productCategorie, String productBrand, Integer productPrice, Integer quantity, Integer weight,
            Status status, Status statusInteg, String tracking, String trackingInteg,
            Integer time, Integer integOrderId, List<String> pictures, List<BasicOrderMessage> messages) {
        this.orderProductId = orderProductId;
        this.productId = productId;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productCategorie = productCategorie;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.weight = weight;
        this.status = status;
        this.statusInteg = statusInteg;
        this.tracking = tracking;
        this.trackingInteg = trackingInteg;
        this.time = time;
        this.integOrderId = integOrderId;
        this.pictures = pictures;
        this.messages = messages;
    }
}

class EstimadoBody {
    public Integer categoryId;
    public Double salePrice;
    public Double boughtPrice;
    public Double porcentage;
    public Integer quantity;
    public Double weight;
    public Boolean international;
}

@Introspected
@JsonInclude(Include.ALWAYS)
class OrderFinalResponse {
    public String direction;
    public String state;
    public String city;
    public int zip;
    public int phone;
    public long since;
    public long till;
    public List<OrderProductUni> productList;
}

@Introspected
@JsonInclude(Include.ALWAYS)
class OrderUser {
    public Integer id;
    public String direction;
    public String state;
    public String city;
    public int zip;
    public int phone;
    public long since;
    public long till;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/user/order")
public class OrderController {

    @Inject
    @Client("/")
    HttpClient client;

    private OrderRepo orderRepo;
    private OrderProductRepo orderProductRepo;
    private CartRepo cartRepo;
    private CartIntegRepo cartIntegRepo;
    private IntegOrderProductRepo integOrderProductRepo;
    private StatusRepo statusRepo;
    private UserRepo userRepo;
    private IntegrationRepo integrationRepo;

    public OrderController(OrderRepo orderRepo, OrderProductRepo orderProductRepo,
            CartRepo cartRepo, CartIntegRepo cartIntegRepo, IntegOrderProductRepo integOrderProductRepo,
            StatusRepo statusRepo, UserRepo userRepo, IntegrationRepo integrationRepo) {
        this.orderRepo = orderRepo;
        this.orderProductRepo = orderProductRepo;
        this.cartRepo = cartRepo;
        this.cartIntegRepo = cartIntegRepo;
        this.integOrderProductRepo = integOrderProductRepo;
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
        this.integrationRepo = integrationRepo;
    }

    @Get("/")
    public List<Order> list() {
        return Lists.newArrayList(orderRepo.findAll());
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/user")
    public List<OrderUser> usuarioTodas(Authentication authentication) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        List<OrderUser> r = Lists.newArrayList();

        for (Order o : Lists.newArrayList(orderRepo.findByUserId(userId))) {
            var temp = new OrderUser();

            temp.id = o.id;
            temp.direction = o.direction;
            temp.state = o.state;
            temp.city = o.city;
            temp.zip = o.zip;
            temp.phone = o.phone;
            temp.since = o.since.getTime();
            temp.till = o.till.getTime();

            r.add(temp);
        }

        return r;
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
        orderFinal.since = order.since.getTime();
        orderFinal.till = order.till.getTime();

        List<OrderProductUni> products = new ArrayList<OrderProductUni>();

        for (OrderProduct orderP : order.orderProducts) {
            var pics = new ArrayList<String>();
            for (var p : orderP.product.pictures) {
                pics.add(p.url);
            }
            List<BasicOrderMessage> messages = Lists.newArrayList();
            for (var om : orderP.orderMessages) {
                messages.add(new BasicOrderMessage(om.status.id, om.name));
            }
            products.add(new OrderProductUni(orderP.id, orderP.product.id, orderP.product.name,
                    orderP.product.desc, orderP.product.categorie.id, orderP.product.brand,
                    orderP.product.price, orderP.quantity, orderP.product.weight, orderP.status, null,
                    orderP.tracking, null, orderP.time, null, pics, messages));
        }

        Map<String, Map<?, ?>> allOrderDetails = Maps.newHashMap();

        for (IntegOrderProduct integOrderP : integOrderProductRepo.findByOrderId(id)) {
            if (!allOrderDetails.containsKey(integOrderP.integOrderId)) {
                Map<?, ?> orderDetails = client.toBlocking().retrieve(
                        HttpRequest.GET(integOrderP.integration.request + "/api/order/" + integOrderP.integOrderId),
                        Map.class);

                allOrderDetails.put(integOrderP.integOrderId, orderDetails);
            }

            Map<?, ?> orderDetails = allOrderDetails.get(integOrderP.integOrderId);
            List<?> placedOrderProds;
            if (orderDetails.get("products") instanceof List<?> p) {
                placedOrderProds = p;
            } else {
                placedOrderProds = List.of();
            }

            Map<?, ?> placed = null;
            for (var map : placedOrderProds) {
                if (map instanceof Map<?, ?> m) {
                    if (m.get("id").equals(integOrderP.productId)) {
                        placed = m;
                    }
                }
            }

            if (placed == null) {
                continue;
            }

            products.add(new OrderProductUni(integOrderP.id, integOrderP.productId, (String) placed.get("name"),
                    (String) placed.get("desc"),
                    (String) placed.get("categorie"), (String) placed.get("brand"),
                    (int) Math.round((Double) placed.get("price")),
                    integOrderP.quantity,
                    (Integer) placed.get("weight"),
                    integOrderP.statusLocal, integOrderP.statusInteg,
                    integOrderP.trackingLocal, integOrderP.trackingInteg, integOrderP.timeInteg + integOrderP.timeLocal,
                    integOrderP.id, List.of(), List.of()));
        }

        // for (var p : products) {
        // List<EstimadoBody> temp = Lists.newArrayList();

        // var t = new EstimadoBody();

        // if (p.productCategorie instanceof String) {
        // t.categoryId = 1;
        // } else if (p.productCategorie instanceof Integer v) {
        // t.categoryId = v == null ? 1 : v;
        // } else {
        // t.categoryId = 1;
        // }
        // t.salePrice = p.productPrice.doubleValue();
        // t.boughtPrice = p.productPrice.doubleValue();
        // t.porcentage = 0.30;
        // t.quantity = p.quantity;
        // t.weight = p.weight == null ? 1 : p.weight.doubleValue();
        // t.international = p.integOrderId != null;

        // System.out.println("category: " + t.categoryId);
        // System.out.println("salePrice: " + t.salePrice);
        // System.out.println("boughtPrice: " + t.boughtPrice);
        // System.out.println("porcentage: " + t.porcentage);
        // System.out.println("quantity: " + t.quantity);
        // System.out.println("weight: " + t.weight);
        // System.out.println("international: " + t.international);

        // temp.add(t);

        // var response = client.toBlocking()
        // .retrieve(HttpRequest.POST("http://mycelium-taxes/api/tax/estimate", temp),
        // List.class);

        // if (p.integOrderId == null) {
        // p.productPrice = ((Double) ((Map<?, ?>)
        // response.get(0)).get("tax")).intValue();
        // System.out.println(p.productPrice);
        // } else {
        // p.productPrice = ((Double) ((Map<?, ?>)
        // response.get(0)).get("tax")).intValue();
        // // p.productPrice = (p.productPrice * 8) + ((Integer)((Map<?, ?>)
        // // response.get(0)).get("tax"));
        // }
        // }

        orderFinal.productList = products;

        return orderFinal;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public int create(@Body OrderCreateRequest body, Authentication auth) {
        var userMap = auth.getAttributes();
        var userId = (int) (long) userMap.get("id");

        var newOrder = new Order();
        newOrder.user = userRepo.findById(userId).get();
        newOrder.direction = body.direction;
        newOrder.state = body.state;
        newOrder.city = body.city;
        newOrder.zip = body.zip;
        newOrder.phone = body.phone;
        newOrder.since = body.since;
        newOrder.till = body.till;
        orderRepo.save(newOrder);

        var orderId = newOrder.id;

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

        for (Integration integ : integrationRepo.findByCartIntegUserId(userId)) {
            List<Map<String, Object>> orderProds = Lists.newArrayList();
            for (CartInteg cart : integ.cartInteg) {
                Map<String, Object> map = Maps.newHashMap();

                map.put("id", cart.productId);
                map.put("quantity", cart.quantity);

                orderProds.add(map);
            }

            Map<String, Object> orderReq = Maps.newHashMap();
            orderReq.put("products", orderProds);
            orderReq.put("email", integ.user);
            orderReq.put("password", integ.password);
            var in10days = Instant.now();
            in10days = in10days.plus(10, ChronoUnit.DAYS);
            var in20days = Instant.now();
            in20days = in20days.plus(20, ChronoUnit.DAYS);
            orderReq.put("since", in10days.getEpochSecond() * 1000);
            orderReq.put("till", in20days.getEpochSecond() * 1000);

            Map<?, ?> placedOrderResponse = client.toBlocking()
                    .retrieve(HttpRequest.POST(integ.request + "/api/order/empresarial/order", orderReq), Map.class);
            Map<?, ?> orderDetails = client.toBlocking().retrieve(
                    HttpRequest.GET(integ.request + "/api/order/" + placedOrderResponse.get("id")),
                    Map.class);
            List<?> placedOrderProds;
            if (orderDetails.get("products") instanceof List<?> p) {
                placedOrderProds = p;
            } else {
                placedOrderProds = List.of();
            }

            for (CartInteg cart : integ.cartInteg) {
                Map<?, ?> placed = null;
                for (var map : placedOrderProds) {
                    if (map instanceof Map<?, ?> m) {
                        if (m.get("id").equals(cart.productId)) {
                            placed = m;
                        }
                    }
                }

                if (placed == null) {
                    continue;
                }

                var newIntegOrderProduct = new IntegOrderProduct();
                newIntegOrderProduct.order = newOrder;
                newIntegOrderProduct.productId = cart.productId;
                newIntegOrderProduct.integOrderId = (String) placedOrderResponse.get("id");
                newIntegOrderProduct.price = (int) (Math.round((Double) placed.get("price")));
                newIntegOrderProduct.quantity = cart.quantity;
                newIntegOrderProduct.statusInteg = statusRepo.findById(1).get();
                newIntegOrderProduct.statusLocal = statusRepo.findById(1).get();
                newIntegOrderProduct.trackingInteg = (String) placed.get("tracking");
                newIntegOrderProduct.trackingLocal = trackingNumberString();
                newIntegOrderProduct.timeInteg = 10;
                newIntegOrderProduct.timeLocal = newIntegOrderProduct.timeInteg + 5;
                newIntegOrderProduct.integration = cart.integration;

                integOrderProductRepo.save(newIntegOrderProduct);
            }
        }

        var existingL = cartRepo.findByUserId(userId);
        for (var cart : existingL) {
            cartRepo.delete(cart);
        }

        var existingI = cartIntegRepo.findByUserId(userId);
        for (var cart : existingI) {
            cartIntegRepo.delete(cart);
        }

        return orderId;
    }

    public String trackingNumberString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    @Get("/market")
    public List<?> market() {
        Date yesterday = getYesterday();
        List<IntegOrderProduct> items = integOrderProductRepo.findByCreatedAfter(yesterday);
        List<Map<String, Object>> body = new ArrayList<>();
        for (IntegOrderProduct item : items) {
            Map<String, Object> current = Maps.newHashMap();
            current.put("integ_url", item.integration.request);
            current.put("product_id", item.productId);
            current.put("quantity", item.quantity);
            current.put("price", item.price);
            body.add(current);
        }

        return body;
    }

    @Post("/impuestos")
    public void impuestos(@Body List<OrderImpuestos> body) {
        // List<OrderImpuestos2> b = new ArrayList<>();
        // for (var item : body) {
        // var temp = new OrderImpuestos2();

        // temp.name = item.name;
        // temp.email = "luis@dummy.com";
        // temp.password = "12345";
        // if (item.categoryId instanceof String) {
        // temp.categoryId = 1;
        // } else if (item.categoryId instanceof Integer v) {
        // temp.categoryId = v;
        // } else {
        // temp.categoryId = 1;
        // }
        // temp.boughtPrice = item.boughtPrice;
        // temp.porcentage = item.porcentage;
        // temp.quantity = item.quantity;
        // temp.weight = item.weight;
        // temp.international = item.international;

        // b.add(temp);
        // }
        // var response =
        // client.toBlocking().exchange(HttpRequest.POST("http://mycelium-taxes/api/tax/impuestos",
        // b),
        // null, null);
    }

    @Post("/receipt")
    public HttpResponse<byte[]> recipt(@Body List<OrderEstimado> body) {
        // List<OrderFactura> b = new ArrayList<>();
        // for (var item : body) {
        // var temp = new OrderFactura();

        // var nombre = userRepo.findById(item.userId).get();

        // temp.name = item.productName;
        // temp.username = nombre.name;
        // temp.address = item.address;
        // temp.categoryId = item.categoryId == null ? 1 : item.categoryId;
        // temp.boughtPrice = item.boughtPrice;
        // temp.porcentage = item.porcentage;
        // temp.quantity = item.quantity;
        // temp.weight = item.weight;
        // temp.international = item.international;

        // b.add(temp);
        // }

        // var response =
        // client.toBlocking().exchange(HttpRequest.POST("http://mycelium-taxes/api/tax/pdf",
        // b),
        // byte[].class);
        // return HttpResponse.<byte[]>ok(response.getBody().get());
        return HttpResponse.<byte[]>ok(new byte[] {});
    }

}

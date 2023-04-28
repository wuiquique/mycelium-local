package com.mycelium.local.controller.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.cart.CartRepo;
import com.mycelium.local.repository.cartinteg.CartInteg;
import com.mycelium.local.repository.cartinteg.CartIntegRepo;
import com.mycelium.local.repository.integration.IntegrationRepo;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

class CartCreateRequest {
    @Nullable
    public Integer integrationId;
    public Object productId;
    public int quantity;
}

@Introspected
@JsonInclude(Include.ALWAYS)
class CartUnifiedResponse {
    public Object id;
    public boolean international;
    public Object productId;
    public String name;
    public String description;
    public Integer quantity;
    public Integer category;
    public Integer weight;
    public Integer price;
    public List<String> pictures;

    public CartUnifiedResponse(Object id, boolean international, Object productId, String name, String description,
            Integer quantity, Integer category, Integer weight, Integer price, List<String> pictures) {
        this.id = id;
        this.international = international;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.category = category;
        this.weight = weight;
        this.price = price;
        this.pictures = pictures;
    }

}

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user/cart")
public class CartController {

    @Inject
    @Client("/")
    HttpClient client;

    private CartRepo cartRepo;
    private CartIntegRepo cartIntegRepo;
    private UserRepo userRepo;
    private ProductRepo productRepo;
    private IntegrationRepo integrationRepo;

    public CartController(CartRepo cartRepo, CartIntegRepo cartIntegRepo, UserRepo userRepo, ProductRepo productRepo,
            IntegrationRepo integrationRepo) {
        this.cartRepo = cartRepo;
        this.cartIntegRepo = cartIntegRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.integrationRepo = integrationRepo;
    }

    @Get("/")
    public List<CartUnifiedResponse> list(Authentication authentication) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        List<CartUnifiedResponse> res = new ArrayList<CartUnifiedResponse>();
        for (Cart cart : cartRepo.findByUserId(userId)) {
            var pics = new ArrayList<String>();
            for (var p : cart.product.pictures) {
                pics.add(p.url);
            }
            res.add(new CartUnifiedResponse(cart.id, false, cart.product.id, cart.product.name, cart.product.desc,
                    cart.quantity, cart.product.categorie.id, cart.product.weight, cart.product.price, pics));
        }
        for (CartInteg cart : cartIntegRepo.findByUserId(userId)) {
            Map<Object, Object> productDetails = client.toBlocking()
                    .retrieve(HttpRequest.GET(cart.integration.request + "/api/products/" + cart.productId), Map.class);

            res.add(new CartUnifiedResponse(cart.id, true, cart.productId, (String) productDetails.get("name"),
                    (String) productDetails.get("desc"), cart.quantity,
                    (Integer) ((Map<?, ?>) productDetails.get("categorie")).get("$oid"),
                    (int) productDetails.get("weight"), (int) productDetails.get("price"),
                    (List<String>) productDetails.get("pictures")));
        }
        return res;
    }

    @Put("/")
    public List<CartUnifiedResponse> create(Authentication authentication, @Body CartCreateRequest body) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        if (body.integrationId != null) {
            var existing = cartIntegRepo.findByUserIdAndProductId(userId, (String) body.productId);

            for (var cart : existing) {
                cartIntegRepo.delete(cart);
            }

            if (body.quantity > 0) {
                var newCart = new CartInteg();
                newCart.productId = (String) body.productId;
                newCart.quantity = body.quantity;
                newCart.user = userRepo.findById(userId).get();
                newCart.integration = integrationRepo.findById(body.integrationId).get();
                cartIntegRepo.save(newCart);
            }
        } else {
            var existing = cartRepo.findByUserIdAndProductId(userId, (int) body.productId);

            for (var cart : existing) {
                cartRepo.delete(cart);
            }

            if (body.quantity > 0) {
                var newCart = new Cart();
                newCart.product = productRepo.findById((int) body.productId).get();
                newCart.quantity = body.quantity;
                newCart.user = userRepo.findById(userId).get();
                cartRepo.save(newCart);
            }
        }

        return list(authentication);
    }
}
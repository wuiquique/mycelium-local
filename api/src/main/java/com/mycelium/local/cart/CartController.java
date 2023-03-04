package com.mycelium.local.cart;

import java.util.ArrayList;
import java.util.List;

import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.cart.CartRepo;
import com.mycelium.local.repository.cartinteg.CartInteg;
import com.mycelium.local.repository.cartinteg.CartIntegRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

class CartCreateRequest {
    public boolean international;
    public int productId;
    public int quantity;
}

class CartUnifiedResponse {
    public Integer id;
    public boolean international;
    public int productId;
    public int quantity;
    public int userId;

    public CartUnifiedResponse(Integer id, boolean international, int productId, int quantity, int userId) {
        this.international = international;
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }
}

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user/cart")
public class CartController {

    private CartRepo cartRepo;
    private CartIntegRepo cartIntegRepo;

    public CartController(CartRepo cartRepo, CartIntegRepo cartIntegRepo) {
        this.cartRepo = cartRepo;
        this.cartIntegRepo = cartIntegRepo;
    }

    @Get("/")
    public List<CartUnifiedResponse> list(Authentication authentication) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        List<CartUnifiedResponse> res = new ArrayList<CartUnifiedResponse>();
        for (Cart cart : cartRepo.findByUser(userId)) {
            res.add(new CartUnifiedResponse(cart.id, false, cart.productId, cart.quantity, cart.userId));
        }
        for (CartInteg cart : cartIntegRepo.findByUser(userId)) {
            res.add(new CartUnifiedResponse(cart.id, true, cart.productId, cart.quantity, cart.userId));
        }
        return res;
    }

    @Put("/")
    public List<CartUnifiedResponse> create(Authentication authentication, @Body CartCreateRequest body) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        if (body.international) {
            var existing = cartIntegRepo.findByUserAndProduct(userId, body.productId);
            for (var cart : existing) {
                cartIntegRepo.delete(cart.id);
            }
            cartIntegRepo.create(body.productId, body.quantity, userId);
        } else {
            var existing = cartRepo.findByUserAndProduct(userId, body.productId);
            for (var cart : existing) {
                cartRepo.delete(cart.id);
            }
            cartRepo.create(body.productId, body.quantity, userId);
        }

        return list(authentication);
    }
}
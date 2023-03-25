package com.mycelium.local.controller.cart;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.cart.CartRepo;
import com.mycelium.local.repository.cartinteg.CartInteg;
import com.mycelium.local.repository.cartinteg.CartIntegRepo;

import io.micronaut.core.annotation.Introspected;
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

@Introspected
@JsonInclude(Include.ALWAYS)
class CartUnifiedResponse {
    public Integer id;
    public boolean international;
    public int productId;
    public String name;
    public String description;
    public int quantity;
    public String category;
    public int weight;
    public int price;
    public List<String> pictures;

    public CartUnifiedResponse(Integer id, boolean international, int productId, String name, String description,
            int quantity, String category, int weight, int price, List<String> pictures) {
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
        for (Cart cart : cartRepo.findByUserId(userId)) {
            // var pics = new ArrayList<String>();
            // for (var p : cart.product.pictures) {
            // pics.add(p.url);
            // }
            res.add(new CartUnifiedResponse(cart.id, false, cart.product.id, cart.product.name, cart.product.desc,
                    cart.quantity, cart.product.categorie.name, cart.product.weight, cart.product.price, List.of()));
        }
        for (CartInteg cart : cartIntegRepo.findByUserId(userId)) {
            res.add(new CartUnifiedResponse(cart.id, true, cart.productId, "Ejemplo Internacional",
                    "Ejemplo Internacional", cart.quantity, "Ejemplo Internacional", 10, 10, List.of()));
        }
        return res;
    }

    @Put("/")
    public List<CartUnifiedResponse> create(Authentication authentication, @Body CartCreateRequest body) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        if (body.international) {
            var existing = cartIntegRepo.findByUserIdAndProductId(userId, body.productId);

            for (var cart : existing) {
                cartIntegRepo.delete(cart);
            }

            if (body.quantity > 0) {
                var newCart = new CartInteg();
                newCart.productId = body.productId;
                newCart.quantity = body.quantity;
                newCart.user.id = userId;
                cartIntegRepo.save(newCart);
            }
        } else {
            var existing = cartRepo.findByUserIdAndProductId(userId, body.productId);

            for (var cart : existing) {
                cartRepo.delete(cart);
            }

            if (body.quantity > 0) {
                var newCart = new Cart();
                newCart.product.id = body.productId;
                newCart.quantity = body.quantity;
                newCart.user.id = userId;
                cartRepo.save(newCart);
            }
        }

        return list(authentication);
    }
}
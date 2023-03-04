package com.mycelium.local.cart;

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
        for (Cart cart : cartRepo.findByUser(userId)) {
            var pics = new ArrayList<String>();
            for (var p : cart.product.pictures) {
                pics.add(p.url);
            }
            res.add(new CartUnifiedResponse(cart.id, false, cart.productId, cart.product.name, cart.product.desc,
                    cart.quantity, cart.product.categorie.name, cart.product.weight, cart.product.price, pics));
        }
        for (CartInteg cart : cartIntegRepo.findByUser(userId)) {
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
            var existing = cartIntegRepo.findByUserAndProduct(userId, body.productId);
            for (var cart : existing) {
                cartIntegRepo.delete(cart.id);
            }
            if (body.quantity > 0) {
                cartIntegRepo.create(body.productId, body.quantity, userId);
            }
        } else {
            var existing = cartRepo.findByUserAndProduct(userId, body.productId);
            for (var cart : existing) {
                cartRepo.delete(cart.id);
            }
            if (body.quantity > 0) {
                cartRepo.create(body.productId, body.quantity, userId);
            }
        }

        return list(authentication);
    }
}
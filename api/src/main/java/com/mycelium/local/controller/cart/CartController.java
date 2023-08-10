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
import com.mycelium.local.repository.categorie.CategorieRepo;
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

/**
 * Objeto de request que contiene el ID de la integracion, el objeto del
 * producto y una cantidad
 */
class CartCreateRequest {
    /**
     * ID de integracion puede ser null, lo que indicaria que el producto es del
     * sistema local
     */
    @Nullable
    public Integer integrationId;
    public Object productId;
    public int quantity;
}

/**
 * Objeto que representa los detalles de un producto en el carrito, utilizado
 * para web service que calcula el estimado segun la SAT
 */
class EstimadoBody {
    public Integer categoryId;
    public Double salePrice;
    public Double boughtPrice;
    public Double porcentage;
    public Integer quantity;
    public Double weight;
    public Boolean international;
}

/** Objeto que representa una categoria */
class Category {
    public Integer id;
    public String name;
}

/**
 * Un objeto unificado que representa la informacion de un producto en el
 * carrito
 */
@Introspected
@JsonInclude(Include.ALWAYS)
class CartUnifiedResponse {
    public Object id;
    public Integer integrationId;
    public Object productId;
    public String name;
    public String description;
    public Integer quantity;
    public String category;
    public Object categoryId;
    public Integer weight;
    public Integer price;
    public List<String> pictures;

    /**
     * 
     * Construye un nuevo objeto CartUnifiedResponse con los parametros especificos
     * 
     * @param id            El ID del carrito
     * @param integrationId El ID de la integracion, de ser necesario
     * @param productId     El ID del producto
     * @param name          El nombre del producto
     * @param description   La descripcion del producto
     * @param quantity      La cantidad del producto
     * @param category      La categoria a la que pertenece el producto
     * @param weight        El peso del producto
     * @param price         El precio del producto
     * @param pictures      Una lista de URLs que representan las imagenes del
     *                      producto
     */
    public CartUnifiedResponse(Object id, Integer integrationId, Object productId, String name, String description,
            Integer quantity, String category, Object categoryId, Integer weight, Integer price,
            List<String> pictures) {
        this.id = id;
        this.integrationId = integrationId;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.category = category;
        this.categoryId = categoryId;
        this.weight = weight;
        this.price = price;
        this.pictures = pictures;
    }

}

/**
 * Controlador para gestionar el carrito de compras del usuario.
 */
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
    private CategorieRepo categorieRepo;

    /**
     * Constructor para inyectar los repositorios necesarios.
     */
    public CartController(CartRepo cartRepo, CartIntegRepo cartIntegRepo, UserRepo userRepo, ProductRepo productRepo,
            IntegrationRepo integrationRepo, CategorieRepo categorieRepo) {
        this.cartRepo = cartRepo;
        this.cartIntegRepo = cartIntegRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.integrationRepo = integrationRepo;
        this.categorieRepo = categorieRepo;
    }

    /**
     * Método GET para listar los productos en el carrito del usuario.
     * 
     * @param authentication objeto de autenticación del usuario
     * @return lista de objetos CartUnifiedResponse
     */
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
            res.add(new CartUnifiedResponse(cart.id, null, cart.product.id, cart.product.name, cart.product.desc,
                    cart.quantity, cart.product.categorie.name, cart.product.categorie.id, cart.product.weight,
                    cart.product.price, pics));
        }
        for (CartInteg cart : cartIntegRepo.findByUserId(userId)) {
            Map<Object, Object> productDetails = client.toBlocking()
                    .retrieve(HttpRequest.GET(cart.integration.request + "/api/products/" + cart.productId), Map.class);

            res.add(new CartUnifiedResponse(cart.id, cart.integration.id, cart.productId,
                    (String) productDetails.get("name"),
                    (String) productDetails.get("desc"), cart.quantity,
                    "",
                    (String) ((Map<?, ?>) productDetails.get("categorie")).get("$oid"),
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
                client.toBlocking().retrieve(
                        HttpRequest.PUT(
                                cart.integration.request + "/api/products/local_cart/" + (String) body.productId, null),
                        String.class);

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
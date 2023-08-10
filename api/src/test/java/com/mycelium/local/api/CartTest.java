package com.mycelium.local.api;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.cart.Cart;
import com.mycelium.local.repository.cart.CartRepo;
import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.categorie.CategorieRepo;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.role.Role;
import com.mycelium.local.repository.role.RoleRepo;
import com.mycelium.local.repository.user.User;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class CartTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    RoleRepo roleRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    CategorieRepo categorieRepo;

    @Inject
    ProductRepo productRepo;

    @Inject
    CartRepo cartRepo;

    @BeforeEach
    void beforeTest() {
        var userRole = new Role();
        userRole.id = 1;
        userRole.name = "Común";
        roleRepo.save(userRole);

        var adminRole = new Role();
        adminRole.id = 2;
        adminRole.name = "Administrador";
        roleRepo.save(adminRole);

        var dummyUser = new User();
        dummyUser.name = "Dummy";
        dummyUser.lastname = "Dummy";
        dummyUser.email = "dummy@dummy.com";
        dummyUser.password = "12345";
        dummyUser.role = roleRepo.findById(1).get();

        dummyUser = userRepo.save(dummyUser);

        List<Integer> categoryIds = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            var newCategorie = new Categorie();
            newCategorie.name = "Category " + i;
            newCategorie = categorieRepo.save(newCategorie);
            categoryIds.add(newCategorie.id);
        }

        List<Integer> productIds = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            var newProduct = new Product();
            newProduct.name = "Product " + i;
            newProduct.desc = "Description " + i;
            newProduct.categorie = categorieRepo.findByName("Category 1").get();
            newProduct.brand = "Brand";
            newProduct.weight = 123;
            newProduct.quantity = (i + 1) * 10;
            newProduct.price = 123;
            newProduct = productRepo.save(newProduct);
            productIds.add(newProduct.id);
        }

        for (int i = 0; i < 10; i++) {
            var newCart = new Cart();
            newCart.product = productRepo.findById(productIds.get(0)).get();
            newCart.quantity = 123;
            newCart.user = userRepo.findById(dummyUser.id).get();
            cartRepo.save(newCart);
        }
    }

    @AfterEach
    void afterTest() {
        cartRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        roleRepo.deleteAll();
    }

    String login() {
        final HttpResponse<?> loginRes = client.toBlocking()
                .exchange(HttpRequest.POST("/api/login", Map.of("username", "dummy@dummy.com", "password", "12345")));
        final String token = loginRes.getCookie("JWT").get().getValue();

        return token;
    }

    @Test
    void testCartList() {
        String token = login();

        final List<?> products = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/user/cart").cookie(Cookie.of("JWT", token)), List.class);

        for (var prodItem : products) {
            if (prodItem instanceof Map<?, ?> product) {
                Assertions.assertTrue(product.containsKey("id"));
                Assertions.assertTrue(product.containsKey("integrationId"));
                Assertions.assertTrue(product.containsKey("productId"));
                Assertions.assertTrue(product.containsKey("name"));
                Assertions.assertTrue(product.containsKey("description"));
                Assertions.assertTrue(product.containsKey("quantity"));
                Assertions.assertTrue(product.containsKey("categoryId"));
                Assertions.assertTrue(product.containsKey("weight"));
                Assertions.assertTrue(product.containsKey("price"));
                Assertions.assertTrue(product.containsKey("pictures"));

                if (product.get("pictures") instanceof List<?> pictures) {
                    for (var picItem : pictures) {
                        Assertions.assertTrue(picItem instanceof String);
                    }
                } else {
                    Assertions.fail();
                }
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testPutCartList() {
        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(
                        HttpRequest.PUT("/api/user/cart", Map.of("international", false, "productId", 1, "quantity", 1))
                                .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetUserOrder() {
        var token = login();

        final List<?> orders = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/user/order").cookie(Cookie.of("JWT", token)), List.class);
        for (var item : orders) {
            if (item instanceof Map<?, ?> ord) {
                Assertions.assertTrue(ord.containsKey("id"));
                Assertions.assertTrue(ord.containsKey("direction"));
                Assertions.assertTrue(ord.containsKey("state"));
                Assertions.assertTrue(ord.containsKey("city"));
                Assertions.assertTrue(ord.containsKey("zip"));
                Assertions.assertTrue(ord.containsKey("phone"));
                Assertions.assertTrue(ord.containsKey("since"));
                Assertions.assertTrue(ord.containsKey("till"));
            } else {
                Assertions.fail();
            }
        }
    }
}

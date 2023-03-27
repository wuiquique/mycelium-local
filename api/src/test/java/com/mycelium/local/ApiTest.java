package com.mycelium.local;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class ApiTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    HttpClient client;

    String login() {
        final HttpResponse<?> loginRes = client.toBlocking()
                .exchange(HttpRequest.POST("/login", Map.of("username", "dummy@dummy.com", "password", "12345")));
        final String token = loginRes.getCookie("JWT").get().getValue();

        return token;
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testProductList() {
        final List<?> products = client.toBlocking().retrieve(HttpRequest.GET("/product"), List.class);

        for (var prodItem : products) {
            if (prodItem instanceof Map<?, ?> product) {
                Assertions.assertTrue(product.containsKey("id"));
                Assertions.assertTrue(product.containsKey("name"));
                Assertions.assertTrue(product.containsKey("desc"));
                Assertions.assertTrue(product.containsKey("categorieId"));
                Assertions.assertTrue(product.containsKey("brand"));
                Assertions.assertTrue(product.containsKey("weight"));
                Assertions.assertTrue(product.containsKey("quantity"));
                Assertions.assertTrue(product.containsKey("price"));
                Assertions.assertTrue(product.containsKey("pictures"));
                Assertions.assertTrue(product.containsKey("technical"));

                if (product.get("pictures") instanceof List<?> pictures) {
                    for (var picItem : pictures) {
                        Assertions.assertTrue(picItem instanceof String);
                    }
                } else {
                    Assertions.fail();
                }

                if (product.get("technical") instanceof List<?> technical) {
                    for (var techItem : technical) {
                        if (techItem instanceof Map<?, ?> tech) {
                            Assertions.assertTrue(tech.containsKey("type"));
                            Assertions.assertTrue(tech.containsKey("value"));
                        } else {
                            Assertions.fail();
                        }
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
    void testCartList() {
        String token = login();

        final List<?> products = client.toBlocking()
                .retrieve(HttpRequest.GET("/user/cart").cookie(Cookie.of("JWT", token)), List.class);

        for (var prodItem : products) {
            if (prodItem instanceof Map<?, ?> product) {
                Assertions.assertTrue(product.containsKey("id"));
                Assertions.assertTrue(product.containsKey("international"));
                Assertions.assertTrue(product.containsKey("productId"));
                Assertions.assertTrue(product.containsKey("name"));
                Assertions.assertTrue(product.containsKey("description"));
                Assertions.assertTrue(product.containsKey("quantity"));
                Assertions.assertTrue(product.containsKey("category"));
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

}

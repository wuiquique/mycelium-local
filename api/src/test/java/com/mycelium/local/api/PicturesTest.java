package com.mycelium.local.api;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
public class PicturesTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    RoleRepo roleRepo;

    @Inject
    UserRepo userRepo;

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

        userRepo.save(dummyUser);
    }

    @AfterEach
    void afterTest() {
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
    void testGetPictures() {
        final List<?> pictures = client.toBlocking().retrieve(HttpRequest.GET("/api/pictures"), List.class);
        for (var item : pictures) {
            if (item instanceof Map<?, ?> pic) {
                Assertions.assertTrue(pic.containsKey("id"));
                Assertions.assertTrue(pic.containsKey("url"));
                Assertions.assertTrue(pic.containsKey("product"));
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testPostPictures() {
        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(HttpRequest.POST("/api/pictures", Map.of("url", "http://Dummy.com", "productId", "1"))
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetPicturesProducts() {
        final List<?> pictures = client.toBlocking().retrieve(HttpRequest.GET("/api/pictures/product/1"), List.class);
        for (var i : pictures) {
            if (i instanceof Map<?, ?> pic) {
                Assertions.assertTrue(pic.containsKey("id"));
                Assertions.assertTrue(pic.containsKey("url"));
                Assertions.assertTrue(pic.containsKey("product"));
            } else {
                Assertions.fail();
            }
        }
    }
}

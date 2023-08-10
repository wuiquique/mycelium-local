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
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class UserTest {

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
    void testRegister() {
        final HttpResponse<Map<?, ?>> response = client.toBlocking().exchange(HttpRequest.POST("/api/register",
                Map.of("username", "Dummy", "password", "12345", "name", "Dummy", "lastname", "dummy")));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetRoles() {
        final List<?> roles = client.toBlocking().retrieve(HttpRequest.GET("/api/role"), List.class);
        for (var i : roles) {
            if (i instanceof Map<?, ?> role) {
                Assertions.assertTrue(role.containsKey("id"));
                Assertions.assertTrue(role.containsKey("name"));
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testPutEditUser() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.PUT("/api/user/1",
                Map.of("name", "Dummy", "lastname", "Dummy", "email", "dummy@dummy.com", "roleId", 1)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }
}

package com.mycelium.local.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.integration.Integration;
import com.mycelium.local.repository.integration.IntegrationRepo;
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
public class IntegrationTest {

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
    IntegrationRepo integrationRepo;
    List<Integer> integrationIds = Lists.newArrayList();

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

        for (int i = 0; i < 10; i++) {
            var newIntegration = new Integration();
            newIntegration.name = "Integration " + i;
            newIntegration.request = "http://example.com/";
            newIntegration.user = "123";
            newIntegration.password = "123";
            newIntegration = integrationRepo.save(newIntegration);
            integrationIds.add(newIntegration.id);
        }
    }

    @AfterEach
    void afterTest() {
        integrationIds.clear();
        integrationRepo.deleteAll();
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
    void testPostIntegration() {
        var token = login();

        final List<?> integrations = client.toBlocking()
                .retrieve(HttpRequest
                        .POST("/api/integration",
                                Map.of("name", "Dummy", "request", "Dummy", "user", "Dummy", "password", "12345"))
                        .cookie(Cookie.of("JWT", token)), List.class);
        for (var i : integrations) {
            if (i instanceof Map<?, ?> integ) {
                Assertions.assertTrue(integ.containsKey("id"));
                Assertions.assertTrue(integ.containsKey("name"));
                Assertions.assertTrue(integ.containsKey("request"));
                Assertions.assertTrue(integ.containsKey("user"));
                Assertions.assertTrue(integ.containsKey("password"));
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testGetIntegrationSpecific() {
        var token = login();

        final Map<?, ?> integration = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/integration/" + integrationIds.get(0)).cookie(Cookie.of("JWT", token)),
                        Map.class);
        Assertions.assertTrue(integration.containsKey("id"));
        Assertions.assertTrue(integration.containsKey("name"));
        Assertions.assertTrue(integration.containsKey("request"));
        Assertions.assertTrue(integration.containsKey("user"));
        Assertions.assertTrue(integration.containsKey("password"));
    }

    @Test
    void testPutIntegration() {
        var token = login();

        var obj = new HashMap<String, Object>();
        obj.put("name", "Dummy");
        obj.put("request", "Dummy");
        obj.put("user", "Dummy");
        obj.put("password", "Dummy");
        final HttpResponse<?> integration = client.toBlocking()
                .exchange(HttpRequest.PUT("/api/integration/" + integrationIds.get(0), obj)
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(integration.getStatus() == HttpStatus.OK);
    }

    @Test
    void testAllIntegrations() {
        String token = login();

        final List<?> integrations = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/integration").cookie(Cookie.of("JWT", token)), List.class);
        for (var i : integrations) {
            if (i instanceof Map<?, ?> integ) {
                Assertions.assertTrue(integ.containsKey("id"));
                Assertions.assertTrue(integ.containsKey("name"));
                Assertions.assertTrue(integ.containsKey("request"));
                Assertions.assertTrue(integ.containsKey("user"));
                Assertions.assertTrue(integ.containsKey("password"));
            } else {
                Assertions.fail();
            }
        }

    }
}

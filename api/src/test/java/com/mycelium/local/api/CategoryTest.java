package com.mycelium.local.api;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.categorie.CategorieRepo;
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
public class CategoryTest {

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
            var newCategorie = new Categorie();
            newCategorie.name = "Category " + i;
            categorieRepo.save(newCategorie);
        }
    }

    @AfterEach
    void afterTest() {
        categorieRepo.deleteAll();
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
    void testCategorieList() {
        final List<?> categories = client.toBlocking().retrieve(HttpRequest.GET("/api/categories"), List.class);

        for (var catItem : categories) {
            if (catItem instanceof Map<?, ?> cate) {
                Assertions.assertTrue(cate.containsKey("id"));
                Assertions.assertTrue(cate.containsKey("name"));
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testPostCategorie() {
        var token = login();

        final HttpResponse<Integer> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/categories", Map.of("name", "Dummy")).cookie(Cookie.of("JWT", token)),
                Integer.class);
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetCategorieSpecific() {
        Integer id = categorieRepo.findByName("Category 1").get().id;

        final Map<?, ?> categorie = client.toBlocking().retrieve(HttpRequest.GET("/api/categories/" + id), Map.class);
        Assertions.assertTrue(categorie.containsKey("id"));
        Assertions.assertTrue(categorie.containsKey("name"));
    }

    @Test
    void testPutCategorieSpecific() {
        Integer id = categorieRepo.findByName("Category 1").get().id;

        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(
                        HttpRequest.PUT("/api/categories/" + id, Map.of("name", "Dummy"))
                                .cookie(Cookie.of("JWT", token)));

        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

}

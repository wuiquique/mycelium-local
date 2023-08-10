package com.mycelium.local.api;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.categorie.CategorieRepo;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;
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

    @Inject
    CategorieRepo categorieRepo;
    List<Integer> categoryIds = Lists.newArrayList();

    @Inject
    ProductRepo productRepo;
    List<Integer> productIds = Lists.newArrayList();

    @Inject
    PictureRepo pictureRepo;
    List<Integer> pictureIds = Lists.newArrayList();

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
            newCategorie = categorieRepo.save(newCategorie);
            categoryIds.add(newCategorie.id);
        }

        for (int i = 0; i < 10; i++) {
            var newProduct = new Product();
            newProduct.name = "Product " + i;
            newProduct.desc = "Description " + i;
            newProduct.categorie = categorieRepo.findById(categoryIds.get(i)).get();
            newProduct.brand = "Brand";
            newProduct.weight = 123;
            newProduct.quantity = (i + 1) * 10;
            newProduct.price = 123;
            newProduct = productRepo.save(newProduct);
            productIds.add(newProduct.id);
        }

        for (int i = 0; i < 10; i++) {
            var newPicture = new Picture();
            newPicture.url = "http://example.com/?" + i;
            newPicture.product = productRepo.findById(productIds.get(i)).get();
            newPicture = pictureRepo.save(newPicture);
            pictureIds.add(newPicture.id);
        }
    }

    @AfterEach
    void afterTest() {
        pictureIds.clear();
        pictureRepo.deleteAll();
        productIds.clear();
        productRepo.deleteAll();
        categoryIds.clear();
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
                .exchange(HttpRequest
                        .POST("/api/pictures", Map.of("url", "http://example.com", "productId", productIds.get(0)))
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetPicturesProducts() {
        final List<?> pictures = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/pictures/product/" + productIds.get(0)), List.class);
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

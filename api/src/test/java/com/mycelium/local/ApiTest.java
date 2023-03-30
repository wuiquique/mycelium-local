package com.mycelium.local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void testCategorieList() {
        final List<?> categories = client.toBlocking().retrieve(HttpRequest.GET("/categories"), List.class);

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
                HttpRequest.POST("/categories", Map.of("name", "Dummy")).cookie(Cookie.of("JWT", token)),
                Integer.class);
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetCategorieSpecific() {
        final Map<?, ?> categorie = client.toBlocking().retrieve(HttpRequest.GET("/categories/1"), Map.class);
        Assertions.assertTrue(categorie.containsKey("id"));
        Assertions.assertTrue(categorie.containsKey("name"));
    }

    @Test
    void testPutCategorieSpecific() {
        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(HttpRequest.PUT("/categories/1", Map.of("name", "Dummy")).cookie(Cookie.of("JWT", token)));

        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testAllIntegrations() {
        String token = login();

        final List<?> integrations = client.toBlocking()
                .retrieve(HttpRequest.GET("/integration").cookie(Cookie.of("JWT", token)), List.class);
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
    void testPostIntegration() {
        var token = login();

        final List<?> integrations = client.toBlocking()
                .retrieve(HttpRequest
                        .POST("/integration",
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
                .retrieve(HttpRequest.GET("/integration/1").cookie(Cookie.of("JWT", token)), Map.class);
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
                .exchange(HttpRequest.PUT("/integration/1", obj).cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(integration.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetPictures() {
        final List<?> pictures = client.toBlocking().retrieve(HttpRequest.GET("/pictures"), List.class);
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
                .exchange(HttpRequest.POST("/pictures", Map.of("url", "http://Dummy.com", "productId", "1"))
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetPicturesProducts() {
        final List<?> pictures = client.toBlocking().retrieve(HttpRequest.GET("/pictures/product/1"), List.class);
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
    void testGetProductByCategoyId() {
        final List<?> products = client.toBlocking().retrieve(HttpRequest.GET("/product/byCategory/1"), List.class);

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
    void testGetProductLastBought() {
        final List<?> products = client.toBlocking().retrieve(HttpRequest.GET("/product/lastBought"), List.class);
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
    void testGetProductRating() {
        final List<?> ratings = client.toBlocking().retrieve(HttpRequest.GET("/product/rating"), List.class);
        for (var item : ratings) {
            if (item instanceof Map<?, ?> rat) {
                Assertions.assertTrue(rat.containsKey("id"));
                Assertions.assertTrue(rat.containsKey("rating"));
                Assertions.assertTrue(rat.containsKey("user"));
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testPostProductRating() {
        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(HttpRequest.POST("/product/rating", Map.of("userId", 1, "productId", 1, "rating", 1))
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetProductRatingAvg() {
        final HttpResponse<Integer> response = client.toBlocking().exchange(HttpRequest.GET("/product/rating/avg/1"),
                Integer.class);
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetProductRatingSpecific() {
        final List<?> ratings = client.toBlocking().retrieve(HttpRequest.GET("/product/rating/1"), List.class);
        for (var item : ratings) {
            if (item instanceof Map<?, ?> rat) {
                Assertions.assertTrue(rat.containsKey("id"));
                Assertions.assertTrue(rat.containsKey("rating"));
                Assertions.assertTrue(rat.containsKey("user"));
            } else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testGetProductTopSales() {
        final List<?> products = client.toBlocking().retrieve(HttpRequest.GET("/product/lastBought"), List.class);
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
    void testProductSpecific() {
        final Map<?, ?> product = client.toBlocking().retrieve(HttpRequest.GET("/product/1"), Map.class);
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
    }

    @Test
    void testRegister() {
        final HttpResponse<Map<?, ?>> response = client.toBlocking().exchange(HttpRequest.POST("/register",
                Map.of("username", "Dummy", "password", "12345", "name", "Dummy", "lastname", "dummy")));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetRoles() {
        final List<?> roles = client.toBlocking().retrieve(HttpRequest.GET("/role"), List.class);
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
    void testPutText() {
        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(HttpRequest.PUT("text", Map.of("component", "Dummy", "key", "Dummy", "value", "Dummy"))
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
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

    @Test
    void testPutCartList() {
        var token = login();

        final HttpResponse<?> response = client.toBlocking()
                .exchange(HttpRequest.PUT("/user/cart", Map.of("international", false, "productId", 1, "quantity", 1))
                        .cookie(Cookie.of("JWT", token)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetUserOrder() {
        var token = login();

        final List<?> orders = client.toBlocking()
                .retrieve(HttpRequest.GET("/user/order").cookie(Cookie.of("JWT", token)), List.class);
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

    @Test
    void testPutEditUser() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.PUT("/user/1",
                Map.of("name", "Dummy", "lastname", "Dummy", "email", "dummy@dummy.com", "roleId", 1)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }
}

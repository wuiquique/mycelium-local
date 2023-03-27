package com.mycelium.local;

import java.util.ArrayList;
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
            }
            else {
                Assertions.fail();
            }
        }
    }

    @Test void testPostCategorie() {
        final HttpResponse<Integer> response = client.toBlocking().exchange(HttpRequest.POST("/categories", Map.of("name", "Dummy")), Integer.class);
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test void testGetCategorieSpecific() {        
        final Map<?, ?> categorie = client.toBlocking().retrieve(HttpRequest.GET("/categories/1"), Map.class);
        Assertions.assertTrue(categorie.containsKey("id"));
        Assertions.assertTrue(categorie.containsKey("name"));    
    }

    @Test 
    void testPutCategorieSpecific() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.PUT("/categories/1", Map.of("name", "Dummy")));

        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

//####################Comments#############################

//#########################################################

    @Test 
    void testAllIntegrations() {
        final List<?> integrations = client.toBlocking().retrieve(HttpRequest.GET("/integration"), List.class);
        for (var i : integrations) {
            if (i instanceof Map<?, ?> integ) {
                Assertions.assertTrue(integ.containsKey("id"));
                Assertions.assertTrue(integ.containsKey("name"));
                Assertions.assertTrue(integ.containsKey("request"));
                Assertions.assertTrue(integ.containsKey("user"));
                Assertions.assertTrue(integ.containsKey("password"));
            }
            else {
                Assertions.fail();
            }
        }
        
    }

    @Test
    void testPostIntegration() {
        final List<?> integrations = client.toBlocking().retrieve(HttpRequest.POST("/integration", Map.of("name", "Dummy", "request", "Dummy", "user", "Dummy", "password", "12345")), List.class);
        for (var i : integrations) {
            if (i instanceof Map<?, ?> integ) {
                Assertions.assertTrue(integ.containsKey("id"));
                Assertions.assertTrue(integ.containsKey("name"));
                Assertions.assertTrue(integ.containsKey("request"));
                Assertions.assertTrue(integ.containsKey("user"));
                Assertions.assertTrue(integ.containsKey("password"));
            }
            else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testGetIntegrationSpecific() {
        final Map<?, ?> integration = client.toBlocking().retrieve(HttpRequest.GET("/integration/1"), Map.class);
        Assertions.assertTrue(integration.containsKey("id"));
        Assertions.assertTrue(integration.containsKey("name"));
        Assertions.assertTrue(integration.containsKey("request"));
        Assertions.assertTrue(integration.containsKey("user"));
        Assertions.assertTrue(integration.containsKey("password"));
    }

    @Test
    void testPutIntegration() {
        
        var obj = new HashMap<String, Object>();
        obj.put("name", "Dummy");
        obj.put("request", "Dummy");
        obj.put("user", "Dummy");
        obj.put("password", "Dummy");
        final HttpResponse<?> integration = client.toBlocking().exchange(HttpRequest.PUT("/integration/1", obj));
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
            }
            else {
                Assertions.fail();
            }
        }
    }

    @Test 
    void testPostPictures() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.POST("/pictures", Map.of("url", "http://Dummy.com", "productId", "1")));
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
            }
            else {
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
    void testPostProduct() {
        var obj = new HashMap<String, Object>();
        obj.put("name", "Dummy");
        obj.put("desc", "Dummy");
        obj.put("categorie", 1);
        obj.put("brand", "Dummy");
        obj.put("weight", 1);
        obj.put("quantity", 1);
        obj.put("price", 1);
        
        var pictures = new ArrayList<Map<String, Object>>();
        var pictureObj = new HashMap<String, Object>(); 
        pictureObj.put("url", "Dummy URL"); 
        pictureObj.put("product", 1);
        pictures.add(pictureObj);

        var technical = new ArrayList<Map<String, Object>>();
        var technicalObj = new HashMap<String, Object>(); 
        technicalObj.put("type", "Dummy"); 
        technicalObj.put("product", 1);
        technicalObj.put("value", "Dummy");
        technical.add(pictureObj);
        
        obj.put("pictures", pictures); 
        obj.put("technical", technical); 
        
        final String response = client.toBlocking().retrieve(HttpRequest.POST("/product", obj), String.class);
        if (response != "Success") {
            Assertions.fail();
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
            }
            else {
                Assertions.fail();
            }
        }
    }

    @Test
    void testPostProductRating() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.POST("/product/rating", Map.of("userId", 1, "productId", 1, "rating", 1)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testGetProductRatingAvg() {
        final HttpResponse<Integer> response = client.toBlocking().exchange(HttpRequest.GET("/product/rating/avg/1"), Integer.class);
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
    void testPutProductRatingSpecific() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.PUT("/product/rating/1", Map.of("userId", 1, "productId", 1, "rating", 1)));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);
    }

    @Test
    void testSearchProduct() {
        final List<?> products = client.toBlocking().retrieve(HttpRequest.GET("/search?q=assdasd&categories=1"), List.class);
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
    void testPutProductSpecific() {
        var obj = new HashMap<String, Object>();
        obj.put("name", "Dummy");
        obj.put("desc", "Dummy");
        obj.put("categorie", 1);
        obj.put("brand", "Dummy");
        obj.put("weight", 1);
        obj.put("quantity", 1);
        obj.put("price", 1);
        
        var pictures = new ArrayList<Map<String, Object>>();
        var pictureObj = new HashMap<String, Object>(); 
        pictureObj.put("url", "Dummy URL"); 
        pictureObj.put("product", 1);
        pictures.add(pictureObj);

        var technical = new ArrayList<Map<String, Object>>();
        var technicalObj = new HashMap<String, Object>(); 
        technicalObj.put("type", "Dummy"); 
        technicalObj.put("product", 1);
        technicalObj.put("value", "Dummy");
        technical.add(pictureObj);
        
        obj.put("pictures", pictures); 
        obj.put("technical", technical); 
        
        final String response = client.toBlocking().retrieve(HttpRequest.POST("/product", obj), String.class);
        if (response != "Success") {
            Assertions.fail();
        }
    }

    @Test
    void testRegister() {
        final HttpResponse<Map<?, ?>> response = client.toBlocking().exchange(HttpRequest.POST("/register", Map.of("username", "Dummy", "password", "12345", "name", "Dummy", "lastname", "dummy")));
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
    void testGetSession() {
        String token = login();

        final Map<?, ?> session = client.toBlocking().retrieve(HttpRequest.GET("/session").cookie(Cookie.of("JWT", token)), Map.class);
        Assertions.assertTrue(session.containsKey("id"));
        Assertions.assertTrue(session.containsKey("email"));
        Assertions.assertTrue(session.containsKey("name"));
        Assertions.assertTrue(session.containsKey("lastname"));
        Assertions.assertTrue(session.containsKey("roleId"));
        Assertions.assertTrue(session.containsKey("role"));
    }

    @Test
    void testGetSessionUnauthenticated() {
        final Map<?, ?> session = client.toBlocking().retrieve(HttpRequest.GET("/session"), Map.class);
        Assertions.assertTrue(session.containsKey("id"));
    }

    @Test
    void testPutText() {
        final HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.PUT("text", Map.of("component", "Dummy", "key", "Dummy", "value", "Dummy")));
        Assertions.assertTrue(response.getStatus() == HttpStatus.OK);    
    }

    @Test
    void testGetUser() {
        final List<?> users = client.toBlocking().retrieve(HttpRequest.GET("/user"), List.class);
        for (var item : users) {
            if (item instanceof Map<?, ?> user) {
                Assertions.assertTrue(user.containsKey("id"));
                Assertions.assertTrue(user.containsKey("name"));
                Assertions.assertTrue(user.containsKey("lastname"));
                Assertions.assertTrue(user.containsKey("email"));
                Assertions.assertTrue(user.containsKey("roleId"));
                Assertions.assertTrue(user.containsKey("role"));
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

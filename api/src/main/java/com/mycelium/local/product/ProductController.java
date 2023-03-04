package com.mycelium.local.product;

import java.util.List;

import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class ProductCreateRequest {
    public String name;
    public String desc;
    public int categorieId;
    public String brand;
    public int weight;
    public int quantity;
    public int price;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/product")
public class ProductController {

    private ProductRepo productRepo;

    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Get("/")
    public List<Product> list() {
        return productRepo.findAll();
    }

    @Post("/")
    public void create(@Body ProductCreateRequest body) {
        productRepo.save(body.name, body.desc, body.categorieId, body.brand, body.weight, body.quantity, body.price);
    }
}

package com.mycelium.local.product;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
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

@Introspected
@JsonInclude(Include.ALWAYS)
class ProductUnifiedResponse {
    public Integer id;
    public String name;
    public String desc;
    public int categorieId;
    public String brand;
    public int weight;
    public int quantity;
    public int price;
    public List<String> pictures;

    public ProductUnifiedResponse(Integer id, String name, String desc, int categorieId,
            String brand, int weight, int quantity, int price, List<String> pictures) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.categorieId = categorieId;
        this.brand = brand;
        this.weight = weight;
        this.quantity = quantity;
        this.price = price;
        this.pictures = pictures;
    }
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/product")
public class ProductController {

    private ProductRepo productRepo;
    private PictureRepo pictureRepo;

    public ProductController(ProductRepo productRepo, PictureRepo pictureRepo) {
        this.productRepo = productRepo;
        this.pictureRepo = pictureRepo;
    }

    @Get("/")
    public List<Product> list() {
        return productRepo.findAll();
    }

    @Get("/{id}")
    public Product get(int id) {
        return productRepo.findById(id).get();
    }

    @Get("/byCategory/{categorieId}")
    public List<ProductUnifiedResponse> listPCategorie(int categorieId) {
        List<ProductUnifiedResponse> res = new ArrayList<ProductUnifiedResponse>();
        for (Product product : productRepo.findByCategorieId(categorieId)) {
            List<String> urls = new ArrayList<String>();
            for (Picture picture : pictureRepo.findByProductId(product.id)) {
                urls.add(picture.url);
            }
            res.add(new ProductUnifiedResponse(product.id, product.name, product.desc, product.categorieId,
            product.brand, product.weight, product.quantity, product.price, urls));
        }
        return res;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body ProductCreateRequest body) {
        productRepo.create(body.name, body.desc, body.categorieId, body.brand, body.weight, body.quantity, body.price);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void update() {
        // TODO
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        // TODO
    }
}

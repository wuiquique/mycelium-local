package com.mycelium.local.controller.rating;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.rating.Rating;
import com.mycelium.local.repository.rating.RatingRepo;
import com.mycelium.local.repository.user.User;
import com.mycelium.local.repository.user.UserRepo;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class RatingCreateRequest {
    public int userId;
    public int productId;
    public int rating;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/product/rating")
public class RatingController {

    private RatingRepo ratingRepo;
    private UserRepo userRepo;
    private ProductRepo productRepo;

    public RatingController(RatingRepo ratingRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.ratingRepo = ratingRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Get("/")
    public List<Rating> list() {
        return Lists.newArrayList(ratingRepo.findAll());
    }

    @Get("/{id}")
    public List<Rating> get(int id) {
        return ratingRepo.findByProductId(id);
    }

    @Get("avg/{id}")
    public int avg(int id) {
        return ratingRepo.findAvgByPId(id);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(int productId, @Body RatingCreateRequest body) {
        var newRating = new Rating();
        newRating.user = userRepo.findById(body.userId).get();
        newRating.product = productRepo.findById(body.productId).get();
        newRating.rating = body.rating;

        ratingRepo.save(newRating);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(int id, @Body RatingCreateRequest body) {
        var rating = ratingRepo.findById(id).get();
        rating.user.id = body.userId;
        rating.rating = body.rating;
        ratingRepo.update(rating);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        // TODO
    }
}
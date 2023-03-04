package com.mycelium.local.rating;

import java.util.List;

import com.mycelium.local.repository.rating.Rating;
import com.mycelium.local.repository.rating.RatingRepo;

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

    public RatingController(RatingRepo ratingRepo) {
        this.ratingRepo = ratingRepo;
    }

    @Get("/")
    public List<Rating> list() {
        return ratingRepo.findAll();
    }

    @Get("/{id}")
    public Rating get(int id) {
        return ratingRepo.findById(id).get();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/{productId}")
    public void create(int productId, @Body RatingCreateRequest body) {
        ratingRepo.create(body.userId, body.productId, body.rating);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(int id) {
        // ToDO
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        // TODO
    }
}
package com.mycelium.local.rating;

import java.util.List;

import com.google.common.collect.Lists;
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
    @Post("/{productId}")
    public void create(int productId, @Body RatingCreateRequest body) {
        var newRating = new Rating();
        newRating.user.id = body.userId;
        newRating.product.id = body.productId;
        newRating.rating = body.rating;

        ratingRepo.save(newRating);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(int id, @Body RatingCreateRequest body) {
        var rating = ratingRepo.findById(id).get();
        rating.user.id = body.userId;
        rating.rating = body.rating;
        ratingRepo.save(rating);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        // TODO
    }
}
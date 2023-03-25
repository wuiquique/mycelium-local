package com.mycelium.local.controller.picture;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class PictureCreateRequest {
    public String url;
    public int productId;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/pictures")
public class PictureController {

    private PictureRepo pictureRepo;

    public PictureController(PictureRepo pictureRepo) {
        this.pictureRepo = pictureRepo;
    }

    @Get("/")
    public List<Picture> list() {
        return Lists.newArrayList(pictureRepo.findAll());
    }

    @Get("/product/{productId}")
    public List<Picture> list(int productId) {
        return pictureRepo.findByProductId(productId);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body PictureCreateRequest body) {
        var newPicture = new Picture();
        newPicture.url = body.url;
        newPicture.product.id = body.productId;

        pictureRepo.save(newPicture);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void update() {
        // TODO
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/")
    public void delete() {
        // TODO
    }
}
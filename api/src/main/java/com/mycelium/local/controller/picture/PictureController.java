package com.mycelium.local.controller.picture;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;
import com.mycelium.local.repository.product.ProductRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
    Clase que representa una solicitud para crear una nueva imagen.
    */
class PictureCreateRequest {
    public String url;
    public int productId;
}

/**
    Controlador para gestionar las imágenes.
    */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/pictures")
public class PictureController {

    private PictureRepo pictureRepo;
    private ProductRepo productRepo;

    public PictureController(PictureRepo pictureRepo, ProductRepo productRepo) {
        this.pictureRepo = pictureRepo;
        this.productRepo = productRepo;
    }

/**
    Retorna una lista de todas las imágenes.
    @return Una lista de objetos Picture.
    */

    @Get("/")
    public List<Picture> list() {
        return Lists.newArrayList(pictureRepo.findAll());
    }

/**
    Retorna una lista de imágenes asociadas a un producto.
    @param productId El ID del producto.
    @return Una lista de objetos Picture.
    */
    @Get("/product/{productId}")
    public List<Picture> list(int productId) {
        return pictureRepo.findByProductId(productId);
    }

/**
    Crea una nueva imagen.
    @param body La solicitud de creación de la imagen.
    */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body PictureCreateRequest body) {
        var newPicture = new Picture();
        newPicture.url = body.url;
        newPicture.product = productRepo.findById(body.productId).get();

        pictureRepo.save(newPicture);
    }
}
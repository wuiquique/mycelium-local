package com.mycelium.local.controller.rating;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.rating.Rating;
import com.mycelium.local.repository.rating.RatingRepo;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
Representa una solicitud para crear una nueva calificación.
*/
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

    /**
     * Constructor de la clase RatingController.
     * 
     * @param ratingRepo Repositorio de las calificaciones.
     * @param userRepo   Repositorio de los usuarios.
     * @param productRepo Repositorio de los productos.
     */
    public RatingController(RatingRepo ratingRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.ratingRepo = ratingRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    /**
 * Obtiene una lista de todas las calificaciones.
 *
 * @return Lista de calificaciones.
 */
    @Get("/")
    public List<Rating> list() {
        return Lists.newArrayList(ratingRepo.findAll());
    }

    /**
 * Obtiene una lista de las calificaciones de un producto.
 *
 * @param id Identificador del producto.
 * @return Lista de calificaciones del producto.
 */
    @Get("/{id}")
    public List<Rating> get(int id) {
        return ratingRepo.findByProductId(id);
    }

    /**
 * Obtiene el promedio de calificaciones para un producto.
 *
 * @param id Identificador del producto.
 * @return Promedio de calificaciones del producto.
 */
    @Get("avg/{id}")
    public int avg(int id) {
        return ratingRepo.findAvgByPId(id);
    }

    /**
 * Crea una nueva calificación para un producto.
 *
 * @param productId Identificador del producto.
 * @param body      Datos de la nueva calificación.
 */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(int productId, @Body RatingCreateRequest body) {
        var newRating = new Rating();
        newRating.user = userRepo.findById(body.userId).get();
        newRating.product = productRepo.findById(body.productId).get();
        newRating.rating = body.rating;

        ratingRepo.save(newRating);
    }
/**
 * Actualiza una calificación existente.
 *
 * @param id   Identificador de la calificación a actualizar.
 * @param body Nuevos datos de la calificación.
 */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(int id, @Body RatingCreateRequest body) {
        var rating = ratingRepo.findById(id).get();
        rating.user = userRepo.findById(body.userId).get();
        rating.rating = body.rating;
        ratingRepo.update(rating);
    }
}
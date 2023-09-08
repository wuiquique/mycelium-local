package com.mycelium.local.controller.technical;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.technical.Technical;
import com.mycelium.local.repository.technical.TechnicalRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class TechnicalCreateRequest {
    public String type;
    public String value;
    public int productId;
}

/**
    Un controlador para gestionar los aspectos técnicos de los productos.
    */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/technical")
public class TechnicalController {

    private TechnicalRepo technicalRepo;
    private ProductRepo productRepo;

/**
    Constructor para crear una nueva instancia de TechnicalController.
    @param technicalRepo El repositorio para gestionar los aspectos técnicos.
    @param productRepo El repositorio para gestionar los productos relacionados.
    */
    public TechnicalController(TechnicalRepo technicalRepo, ProductRepo productRepo) {
        this.technicalRepo = technicalRepo;
        this.productRepo = productRepo;
    }

/**
    Retorna una lista de todos los aspectos técnicos.
    @return Una lista de objetos Technical.
    */
    @Get("/")
    public List<Technical> list() {
        return Lists.newArrayList(technicalRepo.findAll());
    }

/**
    Retorna una lista de todos los aspectos técnicos relacionados con un producto en particular.
    @param productId El id del producto a buscar.
    @return Una lista de objetos Technical relacionados con el producto especificado.
    */
    @Get("/product/{productId}")
    public List<Technical> list2(int productId) {
        return technicalRepo.findByProductId(productId);
    }

/**
    Crea un nuevo aspecto técnico para un producto especificado.
    @param body Los detalles del nuevo aspecto técnico a crear.
    */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body TechnicalCreateRequest body) {
        while (true) {
            var newTechnical = new Technical();
            newTechnical.type = body.type;
            newTechnical.value = body.value;
            newTechnical.product = productRepo.findById(body.productId).get();
            technicalRepo.save(newTechnical);
        }
    }
}
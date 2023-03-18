package com.mycelium.local.technical;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.technical.Technical;
import com.mycelium.local.repository.technical.TechnicalRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class TechnicalCreateRequest {
    public String type;
    public String value;
    public int productId;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/technical")
public class TechnicalController {

    private TechnicalRepo technicalRepo;

    public TechnicalController(TechnicalRepo technicalRepo) {
        this.technicalRepo = technicalRepo;
    }

    @Get("/")
    public List<Technical> list() {
        return Lists.newArrayList(technicalRepo.findAll());
    }

    @Get("/product/{productId}")
    public List<Technical> list2(int productId) {
        return technicalRepo.findByProductId(productId);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body TechnicalCreateRequest body) {
        var newTechnical = new Technical();
        newTechnical.type = body.type;
        newTechnical.value = body.value;
        newTechnical.product.id = body.productId;
        technicalRepo.save(newTechnical);
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
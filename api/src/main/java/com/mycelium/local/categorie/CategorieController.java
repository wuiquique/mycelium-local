package com.mycelium.local.categorie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.categorie.CategorieRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Introspected
@JsonInclude(Include.ALWAYS)
class CategorieCreateRequest {
    public String name;
}

class CategorieUpdateRequest {
    public String name;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/categories")
public class CategorieController {

    private CategorieRepo categorieRepo;

    public CategorieController(CategorieRepo categorieRepo) {
        this.categorieRepo = categorieRepo;
    }

    @Get("/")
    public List<Categorie> list() {
        return Lists.newArrayList(categorieRepo.findAll());
    }

    @Get("/{id}")
    public Categorie get(int id) {
        return categorieRepo.findById(id).get();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public int create(@Body CategorieCreateRequest body) {
        var newCategorie = new Categorie();
        newCategorie.name = body.name;
        categorieRepo.save(newCategorie);
        return newCategorie.id;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(int id, @Body CategorieUpdateRequest body) {
        var categorie = categorieRepo.findById(id).get();
        categorie.name = body.name;
        categorieRepo.update(categorie);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        categorieRepo.deleteById(id);
    }
}
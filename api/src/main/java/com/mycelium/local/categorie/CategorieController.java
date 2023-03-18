package com.mycelium.local.categorie;

import java.util.List;

import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.categorie.CategorieRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class CategorieCreateRequest {
    public String name;
}

class CategorieUpdateRequest {
    public int id;
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
        return categorieRepo.findAll();
    }

    @Get("/{id}")
    public Categorie get(int id) {
        return categorieRepo.findById(id).get();
    }

   @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public long create(@Body CategorieCreateRequest body) {
        long newCategoryId = categorieRepo.create(body.name);
        return newCategoryId;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(@Body CategorieUpdateRequest body) {
        categorieRepo.update(body.id, body.name);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        categorieRepo.delete(id);
    }
}
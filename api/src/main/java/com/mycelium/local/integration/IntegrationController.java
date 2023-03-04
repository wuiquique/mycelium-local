package com.mycelium.local.integration;

import java.util.List;

import com.mycelium.local.repository.integration.Integration;
import com.mycelium.local.repository.integration.IntegrationRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class IntegrationCreateRequest {
    public String name;
    public String request;
    public String user;
    public String password;
}

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/integration")
public class IntegrationController {

    private IntegrationRepo integrationRepo;

    public IntegrationController(IntegrationRepo integrationRepo) {
        this.integrationRepo = integrationRepo;
    }

    @Get("/")
    public List<Integration> list() {
        return integrationRepo.findAll();
    }

    @Get("/{id}")
    public Integration get(int id) {
        return integrationRepo.findById(id).get();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public void create(@Body IntegrationCreateRequest body) {
        integrationRepo.create(body.name, body.request, body.user, body.password);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void update() {
        // TODO
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        // TODO
    }
}

package com.mycelium.local.controller.integration;

import java.util.List;
import java.time.LocalDate;

import com.google.common.collect.Lists;
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

/**
    Clase que representa una solicitud para crear una integración
    */
class IntegrationCreateRequest {
    public String name;
    public String request;
    public String user;
    public String password;
}

/**
    Controlador de integraciones.
    */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/integration")
public class IntegrationController {

    private IntegrationRepo integrationRepo;

    /**
    Constructor de IntegrationController
    @param integrationRepo El repositorio de integraciones a ser utilizado por el controlador
    */
    public IntegrationController(IntegrationRepo integrationRepo) {
        this.integrationRepo = integrationRepo;
    }

    /**
    Método que devuelve una lista de todas las integraciones existentes.
    @return La lista de integraciones existentes
    */
    @Get("/")
    public List<Integration> list() {
        return Lists.newArrayList(integrationRepo.findAll());
    }

    /**
    Método que devuelve la integración con el id especificado.
    @param id El id de la integración deseada
    @return La integración correspondiente al id especificado
    */
    @Get("/{id}")
    public Integration get(int id) {
        return integrationRepo.findById(id).get();
    }

    /**
    Método para crear una nueva integración.
    @param body La solicitud de creación de la integración
    @return La lista actualizada de integraciones
    */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public List<Integration> create(@Body IntegrationCreateRequest body) {
        var newIntegration = new Integration();
        newIntegration.name = body.name;
        newIntegration.request = body.request;
        newIntegration.user = body.user;
        newIntegration.password = body.password; // TODO: hash
        integrationRepo.save(newIntegration);
        return list();
    }

    /**
    Método para actualizar una integración existente.
    @param id El id de la integración a actualizar
    @param body La solicitud de actualización de la integración
    @return La lista actualizada de integraciones
    */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public List<Integration> update(int id, @Body IntegrationCreateRequest body) {
        // var integration = integrationRepo.findById(id).get();
        // integration.name = body.name;
        // integration.request = body.request;
        // integration.user = body.user;
        // integration.password = body.password; // TODO: hash
        // integrationRepo.update(integration);
        return list();
    }

    /**
    Método para eliminar una integración existente.
    @param id El id de la integración a eliminar
    @return La lista actualizada de integraciones
    */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public List<Integration> delete(int id) {
        integrationRepo.deleteById(id);
        return list();
    }

}

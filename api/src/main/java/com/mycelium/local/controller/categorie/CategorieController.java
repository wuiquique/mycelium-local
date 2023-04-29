package com.mycelium.local.controller.categorie;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.mycelium.local.repository.categorie.Categorie;
import com.mycelium.local.repository.categorie.CategorieRepo;
import com.mycelium.local.repository.integration.IntegrationRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

/**
 * Clase que representa la solicitud de creación de una categoría.
 */
@Introspected
@JsonInclude(Include.ALWAYS)
class CategorieCreateRequest {
    public String name;
}

/**
 * Clase que representa la solicitud de actualización de una categoría.
 */
class CategorieUpdateRequest {
    public String name;
}

/**
 * Clase que representa la respuesta de una categoría.
 */
class CategorieResponse {
    public Object id;
    public String name;

    public CategorieResponse(Object id, String name) {
        this.id = id;
        this.name = name;
    }
}

/**
 * Clase que representa el controlador para las categorías.
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/categories")
public class CategorieController {

    @Inject
    @Client("/")
    HttpClient client;

    private CategorieRepo categorieRepo;
    private IntegrationRepo integrationRepo;

    /**
     * Constructor que inicializa las dependencias del controlador.
     * 
     * @param categorieRepo el repositorio de categorías.
     * @param integrationRepo el repositorio de integraciones.
     */
    public CategorieController(CategorieRepo categorieRepo, IntegrationRepo integrationRepo) {
        this.categorieRepo = categorieRepo;
        this.integrationRepo = integrationRepo;
    }

    /**
     * Método que retorna una lista de categorías que incluye tanto las categorías locales como las integradas.
     * 
     * @return la lista de categorías.
     */
    @Get("/")
    public List<CategorieResponse> list() {
        List<CategorieResponse> categories = Lists.newArrayList();

        for (var cat : categorieRepo.findAll()) {
            categories.add(new CategorieResponse(cat.id, cat.name));
        }

        for (var integ : integrationRepo.findAll()) {
            List<?> integCategories = client.toBlocking()
                    .retrieve(HttpRequest.GET(integ.request + "/api/categorie"), List.class);

            for (var catObj : integCategories) {
                if (catObj instanceof Map<?, ?> cat) {
                    categories.add(new CategorieResponse(cat.get("id"), (String) cat.get("name")));
                }
            }
        }

        return categories;
    }

    /**
     * Método que retorna una lista de las categorías locales.
     * 
     * @return la lista de categorías locales.
     */
    @Get("/local")
    public List<Categorie> listLocal() {
        return Lists.newArrayList(categorieRepo.findAll());
    }

    /**
     * Método que retorna una categoría por su identificador.
     * 
     * @param id el identificador de la categoría.
     * @return la categoría.
     */
    @Get("/{id}")
    public Categorie get(int id) {
        return categorieRepo.findById(id).get();
    }

    /**
     * Método que crea una nueva categoría.
     * 
     * @param body la solicitud de creación de la categoría.
     * @return el identificador de la nueva categoría creada.
     */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public int create(@Body CategorieCreateRequest body) {
        var newCategorie = new Categorie();
        newCategorie.name = body.name;
        categorieRepo.save(newCategorie);
        return newCategorie.id;
    }

    /**
     * Actualiza la categoría con el ID especificado.
     *
     * @param id el ID de la categoría que se actualizará.
     * @param body el cuerpo de la solicitud de actualización de categoría.
     */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public void update(int id, @Body CategorieUpdateRequest body) {
        var categorie = categorieRepo.findById(id).get();
        categorie.name = body.name;
        categorieRepo.update(categorie);
    }

    /**
     * Elimina la categoría con el ID especificado.
     *
     * @param id el ID de la categoría que se eliminará.
     */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        categorieRepo.deleteById(id);
    }
}
package com.mycelium.local.controller.role;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.role.Role;
import com.mycelium.local.repository.role.RoleRepo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
    Un controlador para gestionar los roles de usuarios.
    */

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/role")
public class RoleController {

    private RoleRepo roleRepo;


/**
    Constructor para crear una nueva instancia de RoleController.
    @param roleRepo El repositorio para gestionar los roles de usuarios.
    */
    public RoleController(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

/**
    Retorna una lista de todos los roles de usuarios.
    @return Una lista de objetos Role.
    */
    @Get("/")
    public List<Role> list() {
        return Lists.newArrayList(roleRepo.findAll());
    }
}
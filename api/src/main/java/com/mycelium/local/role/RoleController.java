package com.mycelium.local.role;

import java.util.List;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.role.Role;
import com.mycelium.local.repository.role.RoleRepo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/role")
public class RoleController {

    private RoleRepo roleRepo;

    public RoleController(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Get("/")
    public List<Role> list() {
        return Lists.newArrayList(roleRepo.findAll());
    }
}
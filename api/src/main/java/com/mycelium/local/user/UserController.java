package com.mycelium.local.user;

import java.util.ArrayList;
import java.util.List;

import com.mycelium.local.repository.user.User;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class EditUserDetails {
    public String name;
    public String lastname;
    public String email;
    public int roleId;
}

class UserDetails {
    public int id;
    public String name;
    public String lastname;
    public String email;
    public int roleId;
    public String role;

    public UserDetails(int id, String name, String lastname, String email, int roleId, String role) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roleId = roleId;
        this.role = role;
    }

}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/user")
public class UserController {

    private UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Get("/")
    public List<UserDetails> list() {
        List<UserDetails> res = new ArrayList<UserDetails>();
        for (User user : userRepo.findAll()) {
            res.add(new UserDetails(user.id, user.name, user.lastname, user.email,
                    user.roleId,
                    user.getRole().getName()));
        }
        return res;
    }

    @Put("/{id}")
    public List<UserDetails> changeUser(int id, @Body EditUserDetails editUser) {
        userRepo.update(id, editUser.name, editUser.lastname, editUser.email, editUser.roleId);
        return list();
    }

}
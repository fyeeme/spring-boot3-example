package com.lorraine.security.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    @PermitAll
    public String search() {
        return "hello, world!";
    }

    @GetMapping("/{id}")
    @RolesAllowed("USER")
    public Long get(@PathVariable Long id) {
        return id;
    }
}

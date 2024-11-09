package com.mariaclara.cinevault.controllers;

import com.mariaclara.cinevault.DTOs.UserDTO;
import com.mariaclara.cinevault.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cine/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    ResponseEntity<Void> createNewUser(@RequestBody UserDTO user) {
        userService.createNewUser(user);
        return ResponseEntity.ok().build();
    }
}

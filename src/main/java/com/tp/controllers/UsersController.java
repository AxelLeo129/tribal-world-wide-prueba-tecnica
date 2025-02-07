package com.tp.controllers;

import com.tp.models.Users;
import com.tp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Users> getUsers() {
        return productService.getUsers();
    }

}

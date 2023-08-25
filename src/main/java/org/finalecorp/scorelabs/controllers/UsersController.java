package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.sql.Timestamp;
import java.util.Date;

@RestController
public class UsersController {
    public UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/api/v1/createuser")
    public String register(@RequestBody RegisterForm registerForm){

        userService.createUser(registerForm);
        return "hemlo";
    }

}

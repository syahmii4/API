package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UsersController {
    public UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/rest/auth/createuser")
    public String register(@RequestBody RegisterForm registerForm){

        userService.createUser(registerForm);
        return "User registered";
    }

    @ResponseBody
    @PostMapping("/rest/auth/checkuser")
    public Boolean checkUser(@RequestBody String username){
        return userService.userExists(username);
    }
}

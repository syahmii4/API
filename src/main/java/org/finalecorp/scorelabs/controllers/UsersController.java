package org.finalecorp.scorelabs.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.RolesRepository;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.finalecorp.scorelabs.services.RoleService;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Origin", "Authorization", "Content-Type", "Accept"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
@RestController
public class UsersController {
    public UserService userService;
    public RoleService roleService;
    @Autowired
    public void setUserService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

    @ResponseBody
    @GetMapping("/api/v1/user/getuser")
    public Map<String, String> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String emailAddress = (String) authDetails.get("email");
        String fullName = (String) authDetails.get("fullName");
        String role = (String) authDetails.get("role");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Map<String, String> userResponse = new HashMap<>();

        userResponse.put("username", username);
        userResponse.put("email", emailAddress);
        userResponse.put("fullName", fullName);
        userResponse.put("role", roleService.getRoleName(Integer.parseInt(role)));

        return userResponse;
    }
}

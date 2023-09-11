package org.finalecorp.scorelabs.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.java.Log;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.RolesRepository;
import org.finalecorp.scorelabs.requestObjects.ChangePasswordForm;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.finalecorp.scorelabs.services.RoleService;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Authorization"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
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

    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.OPTIONS, RequestMethod.GET}, exposedHeaders = {"Content-Type"}, allowedHeaders = {"Authorization"}, allowCredentials = "true")
    @ResponseBody
    @GetMapping("/api/v1/user/getuser")
    public ResponseEntity<Map<String, String>> getUserDetails() {
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

        return new ResponseEntity<>(userResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/api/v1/user/changepassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordForm form){
        try {
            Users targetUser = userService.getUserByUsername(form.username);
            if(userService.validatePassword(targetUser, form.oldPassword)){
                userService.changePassword(targetUser, form.newPassword);
                return new ResponseEntity<>("New Password Saved", HttpStatusCode.valueOf(200));
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatusCode.valueOf(403));
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatusCode.valueOf(400));
        }
    }
}

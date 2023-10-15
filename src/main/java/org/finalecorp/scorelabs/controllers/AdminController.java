package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Assignment;
import org.finalecorp.scorelabs.models.Submission;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.requestObjects.CreateAssignmentForm;
import org.finalecorp.scorelabs.requestObjects.EditAssignmentForm;
import org.finalecorp.scorelabs.requestObjects.QuestionsForm;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.finalecorp.scorelabs.responseObjects.AssignmentStream;
import org.finalecorp.scorelabs.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Origin", "Authorization", "Content-Type", "Accept"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
public class AdminController {
    public UserService userService;
    public StudentsService studentsService;
    public TeacherService teacherService;
    public ParentService parentService;

    @Autowired
    public AdminController(UserService userService, StudentsService studentsService, TeacherService teacherService, ParentService parentService) {
        this.userService = userService;
        this.teacherService = teacherService;
        this.studentsService = studentsService;
        this.parentService = parentService;
    }

    @ResponseBody
    @GetMapping("/viewusers")
    public ResponseEntity<List<Users>> viewUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        if (role.equals("4")) {
            List<Users> usersList = userService.getUsers();
            return new ResponseEntity<>(usersList, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(403));
        }
    }

    @ResponseBody
    @PostMapping("/createusers")
    public ResponseEntity<String> createUsers(@RequestBody RegisterForm registerForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");


        if (role.equals("4")) {
            userService.createUser(registerForm);
            return new ResponseEntity<>("user created", HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(403));
        }
    }
}



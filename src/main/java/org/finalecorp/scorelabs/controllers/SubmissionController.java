package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.services.StudentsService;
import org.finalecorp.scorelabs.services.SubmissionService;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/submission")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Origin", "Authorization", "Content-Type", "Accept"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
public class SubmissionController {
    public SubmissionService submissionService;
    public StudentsService studentsService;

    public UserService userService;

    @Autowired
    public SubmissionController(SubmissionService submissionService, StudentsService studentsService, UserService userService){
        this.submissionService=submissionService;
        this.studentsService=studentsService;
        this.userService=userService;
    }

    @ResponseBody
    @PostMapping("/submit")
    public ResponseEntity<Map<Object, Object>> submit(@RequestBody Map<Object, Object> answers){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        if(!role.equals("1")){
            return new ResponseEntity<>((Map<Object,Object>) new HashMap<>().put("res", "Ayamakkk"), HttpStatusCode.valueOf(403));
        }

        int userId = userService.getUserByUsername(username).getUserId();
        int studentId = studentsService.getStudentByUserId(userId).getStudentId();

        try {
            Map<Object, Object> response=submissionService.checkAnswers(answers, studentId);
             return new ResponseEntity<>(response, HttpStatusCode.valueOf(200)) ;
        } catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>((Map<Object,Object>) new HashMap<>().put("res", "uh ohhhhh"), HttpStatusCode.valueOf(400));
        }

    }
}

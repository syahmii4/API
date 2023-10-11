package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Assignment;
import org.finalecorp.scorelabs.models.Submission;
import org.finalecorp.scorelabs.responseObjects.SubmissionsInfo;
import org.finalecorp.scorelabs.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/submission")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Origin", "Authorization", "Content-Type", "Accept"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
public class SubmissionController {
    public SubmissionService submissionService;
    public StudentsService studentsService;
    public AssignmentService assignmentService;
    public UserService userService;
    public TeacherService teacherService;
    public ClassesService classesService;

    @Autowired
    public SubmissionController(SubmissionService submissionService, StudentsService studentsService, AssignmentService assignmentService, UserService userService, TeacherService teacherService, ClassesService classesService){
        this.submissionService=submissionService;
        this.studentsService=studentsService;
        this.assignmentService = assignmentService;
        this.userService=userService;
        this.teacherService=teacherService;
        this.classesService=classesService;
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

    @ResponseBody
    @GetMapping("/view")
    public ResponseEntity<List<SubmissionsInfo>> viewSubmission(@RequestParam int assignmentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        if(role.equals("2")){
            int userId = userService.getUserByUsername(username).getUserId();
            int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();
            Assignment assignment = assignmentService.getAssignmentById(assignmentId);
            if(classesService.teacherIsClassOwner(teacherId, assignment.getClassId())){
                List<SubmissionsInfo> submissionList = submissionService.getSubmissionsByAssignmentId(assignmentId);
                return new ResponseEntity<>(submissionList, HttpStatusCode.valueOf(200));
            }
            else {
                return new ResponseEntity<>(null, HttpStatusCode.valueOf(403));
            }
        }
        else {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(403));
        }
    }

    @ResponseBody
    @GetMapping("/viewstudent")
    public ResponseEntity<List<Submission>> viewSubmissionByStudent(@RequestParam int assignmentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        if(role.equals("1")){
            int userId = userService.getUserByUsername(username).getUserId();
            int studentId = studentsService.getStudentByUserId(userId).getStudentId();
            List<Submission> submissionList = submissionService.getSubmissionsByAssignmentIdAndStudentId(assignmentId, studentId);
            return new ResponseEntity<>(submissionList, HttpStatusCode.valueOf(200));
        }
        else {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(403));
        }
    }
}

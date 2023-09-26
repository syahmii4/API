package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Assignment;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.requestObjects.CreateAssignmentForm;
import org.finalecorp.scorelabs.requestObjects.EditAssignmentForm;
import org.finalecorp.scorelabs.requestObjects.QuestionsForm;
import org.finalecorp.scorelabs.responseObjects.AssignmentStream;
import org.finalecorp.scorelabs.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assignment")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Origin", "Authorization", "Content-Type", "Accept"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
public class AssignmentController {
    public AssignmentService assignmentService;
    public UserService userService;
    public TeacherService teacherService;
    public ClassesService classesService;
    public StudentsService studentsService;
    public ClassroomService classroomService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService, ClassesService classesService, UserService userService, TeacherService teacherService, StudentsService studentsService, ClassroomService classroomService){
        this.assignmentService = assignmentService;
        this.classesService = classesService;
        this.userService = userService;
        this.teacherService = teacherService;
        this.studentsService = studentsService;
        this.classroomService = classroomService;
    }

    @GetMapping("/view")
    @ResponseBody
    public List<Assignment> viewAssignments(@RequestParam int classId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        return assignmentService.getAssignmentByClass(classId);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createAssignment(@RequestBody CreateAssignmentForm form){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        int userId = userService.getUserByUsername(username).getUserId();
        int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();

        if(role.equals("2") && classesService.teacherIsClassOwner(teacherId, form.classId)){
            try {
                assignmentService.createAssignment(form);
                return new ResponseEntity<>("Created a new assignment", HttpStatusCode.valueOf(200));
            }
            catch (Exception e){
                return new ResponseEntity<>("Something went wrong", HttpStatusCode.valueOf(400));
            }
        }
        else {
            return new ResponseEntity<>("Failed to create a new assignment", HttpStatusCode.valueOf(403));
        }
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> editAssignment(@RequestBody EditAssignmentForm form){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        int userId = userService.getUserByUsername(username).getUserId();
        int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();
        int classId = assignmentService.getAssignmentById(form.assignmentId).getClassId();

        if(role.equals("2") && classesService.teacherIsClassOwner(teacherId, classId)){
            assignmentService.editAssignment(form);
            return new ResponseEntity<>("Edited assignment", HttpStatusCode.valueOf(200));
        }
        else {
            return new ResponseEntity<>("Failed to edit assignment", HttpStatusCode.valueOf(403));
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteClass(@RequestBody int assignmentId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            int userId = userService.getUserByUsername(username).getUserId();
            int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();

            if(role.equals("2")) {
                assignmentService.deleteAssignment(assignmentId, teacherId);
                return new ResponseEntity<>("hemmoooooo", HttpStatusCode.valueOf(200));
            }
            else {
                return new ResponseEntity<>("tak boyehhh welkkkkk", HttpStatusCode.valueOf(403));
            }
        }
        catch (Exception e){
            return new ResponseEntity<>("ALAAAAAAAAAAAAAAAAAAAAAA", HttpStatusCode.valueOf(204));
        }
    }

    @PostMapping("/get")
    @ResponseBody
    public Assignment get(@RequestBody int assignmentId) {
        return assignmentService.getAssignmentById(assignmentId);
    }

    @GetMapping("/stream")
    @ResponseBody
    public List<AssignmentStream> stream() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        int userId = userService.getUserByUsername(username).getUserId();
        int studentId = studentsService.getStudentByUserId(userId).getStudentId();

        return assignmentService.getAssignmentByStudent(studentId);
    }

    @PostMapping("/build")
    @ResponseBody
    public ResponseEntity<String> build(@RequestBody QuestionsForm form){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        if(role.equals("2")){
            assignmentService.insertQuestions(form.questions, form.assignmentId);
            return new ResponseEntity<String>("Successful", HttpStatusCode.valueOf(200));
        }
        else {
            return new ResponseEntity<String>("Could not insert questions", HttpStatusCode.valueOf(403));
        }
    }

    @GetMapping("/getquestions")
    @ResponseBody
    public ResponseEntity<Map<Object, Object>> getQuestions(@RequestParam int assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        Map<Object, Object> question;
        ResponseEntity<Map<Object, Object>> response;

        try {
            question = assignmentService.getQuestionByAssignmentId(assignmentId);
            response = new ResponseEntity<>(question, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            System.out.println("AYAMAK" + e.getMessage());
            question = null;
            response = new ResponseEntity<>(question, HttpStatusCode.valueOf(200));
        }
        return response;
    }
}

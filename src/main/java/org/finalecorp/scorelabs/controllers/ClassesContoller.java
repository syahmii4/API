package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Classes;
import org.finalecorp.scorelabs.models.Classroom;
import org.finalecorp.scorelabs.repositories.ClassesRepository;
import org.finalecorp.scorelabs.requestObjects.CreateClassForm;
import org.finalecorp.scorelabs.requestObjects.EditClassForm;
import org.finalecorp.scorelabs.responseObjects.ClassesInfo;
import org.finalecorp.scorelabs.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/classes")
@RestController
@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Origin", "Authorization", "Content-Type", "Accept"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
public class ClassesContoller {
    public ClassesService classesService;
    public TeacherService teacherService;
    public UserService userService;
    public StudentsService studentsService;
    @Autowired
    public ClassesContoller(ClassesService classesService, TeacherService teacherService, UserService userService, StudentsService studentsService){
        this.classesService=classesService;
        this.teacherService=teacherService;
        this.userService=userService;
        this.studentsService=studentsService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createClass(@RequestBody CreateClassForm form){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            int userId = userService.getUserByUsername(username).getUserId();
            int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();

            if(role.equals("2")) {
                classesService.createClasses(form, teacherId);
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

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> editClass(@RequestBody EditClassForm form){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            int userId = userService.getUserByUsername(username).getUserId();
            int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();

            if(role.equals("2")) {
                classesService.editClasses(form, teacherId);
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
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteClass(@RequestBody int classId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            int userId = userService.getUserByUsername(username).getUserId();
            int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();

            if(role.equals("2")) {
                classesService.deleteClasses(classId, teacherId);
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

    @GetMapping("/view")
    @ResponseBody
    public List<ClassesInfo> viewClasses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        int userId = userService.getUserByUsername(username).getUserId();

        switch (role){
            case "1":
                int studentId = studentsService.getStudentByUserId(userId).getStudentId();
                return classesService.getClassesByStudentId(studentId);
            case "2":
                int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();
                return classesService.getClassesByTeacherId(teacherId);
            default:
                return null;
        }
    }

    @GetMapping("/viewclassroom")
    @ResponseBody
    public ResponseEntity<ClassesInfo> viewClass(@RequestParam int classId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String role = (String) authDetails.get("role");

        int userId = userService.getUserByUsername(username).getUserId();

        switch (role){
            case "1":
                int studentId = studentsService.getStudentByUserId(userId).getStudentId();
                ClassesInfo studentClassesInfo = classesService.getStudentsClassByClassId(studentId, classId);
                return new ResponseEntity<ClassesInfo>(studentClassesInfo, HttpStatusCode.valueOf(200));
            case "2":
                int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();
                ClassesInfo teacherClassesInfo = classesService.getTeachersClassByClassId(teacherId, classId);
                return new ResponseEntity<ClassesInfo>(teacherClassesInfo, HttpStatusCode.valueOf(200));

            default:
                return new ResponseEntity<ClassesInfo>(new ClassesInfo(), HttpStatusCode.valueOf(403));
        }
    }
}

package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/classroom")
public class ClassroomController {
    public ClassroomService classroomService;
    public StudentsService studentsService;
    public UserService userService;
    @Autowired
    public ClassroomController(ClassroomService classroomService, StudentsService studentsService, UserService userService){
        this.classroomService=classroomService;
        this.studentsService=studentsService;
        this.userService=userService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addStudent(@RequestBody int classId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            if(!role.equals("1")){
                return new ResponseEntity<>("tak boyehhhhh", HttpStatusCode.valueOf(403));
            }

            int userId = userService.getUserByUsername(username).getUserId();
            int studentId = studentsService.getStudentByUserId(userId).getStudentId();

            if(classroomService.studentIsInClass(classId, studentId)){
                return new ResponseEntity<>("dah ada laaaaa", HttpStatusCode.valueOf(406));
            }

            classroomService.addStudentToClass(classId, studentId);

            return new ResponseEntity<>("YOMKEYYYY", HttpStatusCode.valueOf(200));
        }
        catch (Exception e) {
            return new ResponseEntity<>("Ayamakkkkkk", HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteStudent(@RequestBody int classId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            if(!role.equals("1")){
                return new ResponseEntity<>("tak boyehhhhh", HttpStatusCode.valueOf(403));
            }

            int userId = userService.getUserByUsername(username).getUserId();
            int studentId = studentsService.getStudentByUserId(userId).getStudentId();

            if(classroomService.studentIsInClass(classId, studentId)){
                classroomService.deleteStudentFromClass(classId, studentId);
                return new ResponseEntity<>("YOMKEYYYY", HttpStatusCode.valueOf(200));
            }
            else {
                return new ResponseEntity<>("awak takde pun dalam kelas nie", HttpStatusCode.valueOf(406));
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("Ayamakkkkkk", HttpStatusCode.valueOf(400));
        }
    }
}

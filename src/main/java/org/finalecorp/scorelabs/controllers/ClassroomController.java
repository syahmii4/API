package org.finalecorp.scorelabs.controllers;

import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.requestObjects.ClassroomAddForm;
import org.finalecorp.scorelabs.responseObjects.StudentInfo;
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
@RequestMapping("/api/v1/classroom")
public class ClassroomController {
    public ClassroomService classroomService;
    public StudentsService studentsService;
    public UserService userService;
    public TeacherService teacherService;
    public ClassesService classesService;
    @Autowired
    public ClassroomController(ClassroomService classroomService, StudentsService studentsService, UserService userService, TeacherService teacherService, ClassesService classesService){
        this.classroomService = classroomService;
        this.studentsService = studentsService;
        this.userService = userService;
        this.teacherService = teacherService;
        this.classesService = classesService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addClassStudent(@RequestBody int classId){
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

    @PostMapping("/addstudent")
    @ResponseBody
    public ResponseEntity<String> addStudent(@RequestBody ClassroomAddForm classroomAddForm){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            if(!role.equals("2")){
                return new ResponseEntity<>("tak boyehhhhh", HttpStatusCode.valueOf(403));
            }

            int selfUserId = userService.getUserByUsername(username).getUserId();
            int selfTeacherId = teacherService.getTeacherByUserId(selfUserId).getTeacherId();
            if(classesService.teacherIsClassOwner(selfTeacherId, classroomAddForm.getClassId())){
                Users user = userService.getUserByUsername(classroomAddForm.getClassroomUsername());
                if (user == null) {
                    return new ResponseEntity<>("User not found", HttpStatusCode.valueOf(406));
                }

                int userId = user.getUserId();

                Students student = studentsService.getStudentByUserId(userId);

                if (student == null) {
                    return new ResponseEntity<>("User is not a student", HttpStatusCode.valueOf(406));
                }

                int studentId = student.getStudentId();

                if (classroomService.studentIsInClass(classroomAddForm.getClassId(), studentId)) {
                    return new ResponseEntity<>("Student is already in this class", HttpStatusCode.valueOf(406));
                }

                classroomService.addStudentToClass(classroomAddForm.getClassId(), studentId);

                return new ResponseEntity<>("YOMKEYYYY", HttpStatusCode.valueOf(200));
            }
            else {
                return new ResponseEntity<>("You do not own this class", HttpStatusCode.valueOf(403));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("There was an error", HttpStatusCode.valueOf(400));
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

    @GetMapping("/kick")
    @ResponseBody
    public ResponseEntity<String> deleteStudent(@RequestParam int classId, @RequestParam int studentId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            int userId = userService.getUserByUsername(username).getUserId();
            int teacherId = teacherService.getTeacherByUserId(userId).getTeacherId();

            if(!role.equals("2")){
                return new ResponseEntity<>("tak boyehhhhh", HttpStatusCode.valueOf(403));
            }

            if(!classesService.teacherIsClassOwner(teacherId, classId)){
                return new ResponseEntity<>("tak boyehhhhh", HttpStatusCode.valueOf(403));
            }

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

    @GetMapping("/getstudents")
    @ResponseBody
    public ResponseEntity<List<StudentInfo>> getStudents(@RequestParam int classId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
            String role = (String) authDetails.get("role");

            List<StudentInfo> students = classroomService.getStudentsInfoByClassId(classId);

            return new ResponseEntity<>(students, HttpStatusCode.valueOf(200));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }
    }
}

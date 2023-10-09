package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Classroom;
import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.ClassesRepository;
import org.finalecorp.scorelabs.repositories.ClassroomRepository;
import org.finalecorp.scorelabs.repositories.StudentsRepository;
import org.finalecorp.scorelabs.repositories.UsersRepository;
import org.finalecorp.scorelabs.responseObjects.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final UserService userService;
    private final StudentsService studentsService;
    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, UserService userService, StudentsService studentsService){
        this.classroomRepository = classroomRepository;
        this.userService = userService;
        this.studentsService = studentsService;
    }

    public Boolean studentIsInClass(int classId, int studentId) {
        List<Classroom> classrooms = classroomRepository.findClassroomByStudentId(studentId);

        for (Classroom classroom : classrooms) {
            if(classId == classroom.getClassId()){
                return true;
            }
        }

        return false;
    }

    public int getClassroomIdByClassIdAndStudentId(int classId, int studentId) {
        List<Classroom> classrooms = classroomRepository.findClassroomByStudentId(studentId);

        for (Classroom classroom : classrooms) {
            if(classId == classroom.getClassId()){
                return classroom.getClassroomId();
            }
        }
        return 0;
    }

    public Classroom addStudentToClass(int classId, int studentId) {
        try {
            Classroom classroom = new Classroom();
            classroom.setClassId(classId);
            classroom.setStudentId(studentId);
            classroomRepository.save(classroom);
            return classroom;
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteStudentFromClass(int classId, int studentId) {
        try {
            Classroom classroomToDelete = classroomRepository.findClassroomByClassroomId(getClassroomIdByClassIdAndStudentId(classId, studentId));
            classroomRepository.delete(classroomToDelete);
            return "okieee";
        } catch (Exception e) {
            return null;
        }
    }

    public List<StudentInfo> getStudentsInfoByClassId(int classId) {
        try {
            List<Classroom> students = classroomRepository.findAllClassroomByClassId(classId);
            List<StudentInfo> studentInfoList = new ArrayList<>();
            students.forEach((studentClassroomRel) -> {
                StudentInfo studentInfo = new StudentInfo(studentClassroomRel);
                int userId = studentsService.getStudentByStudentId(studentClassroomRel.getStudentId()).getUserId();
                Users studentUser = userService.getUserByUserId(userId);

                studentInfo.setFullName(studentUser.getFullName());
                studentInfo.setUserName(studentUser.getUsername());
                studentInfo.setProfilePicture(studentUser.getProfilePicture());
                studentInfoList.add(studentInfo);
            });

            return studentInfoList;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}

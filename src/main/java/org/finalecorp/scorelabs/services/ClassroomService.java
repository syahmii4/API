package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Classroom;
import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.repositories.ClassesRepository;
import org.finalecorp.scorelabs.repositories.ClassroomRepository;
import org.finalecorp.scorelabs.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository){
        this.classroomRepository=classroomRepository;
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
}

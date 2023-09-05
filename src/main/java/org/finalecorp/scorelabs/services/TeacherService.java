package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.models.Teacher;
import org.finalecorp.scorelabs.repositories.StudentsRepository;
import org.finalecorp.scorelabs.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    @Autowired
    public TeacherService(TeacherRepository teacherRepository){
        this.teacherRepository=teacherRepository;
    }

    public Teacher createTeacher(int userId){
        try {
            Teacher teacher = new Teacher();
            teacher.setUserId(userId);
            teacherRepository.save(teacher);
            return teacher;
        }
        catch (Exception e){
            return null;
        }
    }

    public Teacher getTeacherByUserId(int userId){
        return teacherRepository.findTeacherByUserId(userId);
    }
}

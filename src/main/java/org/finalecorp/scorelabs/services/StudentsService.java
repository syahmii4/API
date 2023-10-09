package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentsService {
    private final StudentsRepository studentsRepository;
    @Autowired
    public StudentsService(StudentsRepository studentsRepository){
        this.studentsRepository=studentsRepository;
    }

    public Students createStudent(int userId){
        try {
            Students students = new Students();
            students.setUserId(userId);
            students.setPoints(0);
            studentsRepository.save(students);
            return students;
        }
        catch (Exception e){
            return null;
        }
    }

    public Students getStudentByUserId(int userId) {
        return studentsRepository.findStudentsByUserId(userId);
    }

    public Students getStudentByStudentId(int studentId) {
        return studentsRepository.findStudentsByStudentId(studentId);
    }
}

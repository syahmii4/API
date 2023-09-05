package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Classes;
import org.finalecorp.scorelabs.models.Classroom;
import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.repositories.ClassesRepository;
import org.finalecorp.scorelabs.repositories.ClassroomRepository;
import org.finalecorp.scorelabs.repositories.StudentsRepository;
import org.finalecorp.scorelabs.requestObjects.CreateClassForm;
import org.finalecorp.scorelabs.requestObjects.EditClassForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassesService {
    private final ClassesRepository classesRepository;
    private final ClassroomRepository classroomRepository;
    @Autowired
    public ClassesService(ClassesRepository classesRepository, ClassroomRepository classroomRepository){
        this.classesRepository=classesRepository;
        this.classroomRepository=classroomRepository;
    }

    public Classes createClasses(CreateClassForm form, int teacherId){
        try {
            Classes classes = new Classes();
            classes.setTeacherId(teacherId);
            classes.setClassName(form.className);
            classesRepository.save(classes);
            return classes;
        }
        catch (Exception e){
            return null;
        }
    }

    public Classes editClasses(EditClassForm form, int teacherId) {
        try {
            Classes classes = classesRepository.findClassesByClassId(form.classId);
            if(teacherId==classes.getTeacherId()){
                classes.setClassName(form.className);
                classesRepository.save(classes);
                return classes;
            }else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    public String deleteClasses(int classId, int teacherId) {
        try {
            Classes classes = classesRepository.findClassesByClassId(classId);
            if(teacherId==classes.getTeacherId()){
                classesRepository.deleteById(classId);
                return "omkey";
            }else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    public List<Classes> getClassesByStudentId(int studentId){
        List<Classroom> classrooms = classroomRepository.findClassroomByStudentId(studentId);

        List<Classes> classes = new ArrayList<>();

        for (Classroom classroom:classrooms) {
            classes.add(classesRepository.findClassesByClassId(classroom.getClassId()));
        }
        return classes;
    }

    public List<Classes> getClassesByTeacherId(int teacherId){
        return classesRepository.findClassesByTeacherId(teacherId);
    }
}

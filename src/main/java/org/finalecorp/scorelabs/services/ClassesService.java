package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Classes;
import org.finalecorp.scorelabs.models.Classroom;
import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.repositories.*;
import org.finalecorp.scorelabs.requestObjects.CreateClassForm;
import org.finalecorp.scorelabs.requestObjects.EditClassForm;
import org.finalecorp.scorelabs.responseObjects.ClassesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassesService {
    private final ClassesRepository classesRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final UsersRepository usersRepository;
    @Autowired
    public ClassesService(ClassesRepository classesRepository, ClassroomRepository classroomRepository, TeacherRepository teacherRepository, UsersRepository usersRepository){
        this.classesRepository=classesRepository;
        this.classroomRepository=classroomRepository;
        this.teacherRepository=teacherRepository;
        this.usersRepository=usersRepository;
    }

    public Classes createClasses(CreateClassForm form, int teacherId){
        try {
            Classes classes = new Classes();
            classes.setTeacherId(teacherId);
            classes.setClassName(form.className);
            classes.setDisplayColor(form.displayColor);
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
                classes.setDisplayColor(form.displayColor);
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
                classesRepository.delete(classes);
                return "omkey";
            }else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    public List<ClassesInfo> getClassesByStudentId(int studentId){
        List<Classroom> classrooms = classroomRepository.findClassroomByStudentId(studentId);

        List<ClassesInfo> classes = new ArrayList<>();

        for (Classroom classroom:classrooms) {
            Classes classesTemp = classesRepository.findClassesByClassId(classroom.getClassId());
            ClassesInfo classesInfo = new ClassesInfo(classesTemp);

            int teacherUserId = teacherRepository.findTeacherByTeacherId(classesTemp.getTeacherId()).getUserId();

            classesInfo.setTeacherName(usersRepository.findUsersByUserId(teacherUserId).getFullName());
            classes.add(classesInfo);
        }
        return classes;
    }

    public List<ClassesInfo> getClassesByTeacherId(int teacherId){
        List<Classes> classes = classesRepository.findClassesByTeacherId(teacherId);
        List<ClassesInfo> classesInfoList = new ArrayList<>();

        for (Classes classObj:classes){
            ClassesInfo classesInfo = new ClassesInfo(classObj);
            int teacherUserId = teacherRepository.findTeacherByTeacherId(teacherId).getUserId();

            classesInfo.setTeacherName(usersRepository.findUsersByUserId(teacherUserId).getFullName());
            classesInfoList.add(classesInfo);
        }

        return classesInfoList;
    }

    public ClassesInfo getStudentsClassByClassId(int studentId, int classId) {
        if(classroomRepository.findClassroomByStudentIdAndClassId(studentId, classId) != null){
            Classes classes = classesRepository.findClassesByClassId(classId);
            ClassesInfo classesInfo = new ClassesInfo(classes);
            int teacherUserId = teacherRepository.findTeacherByTeacherId(classes.getTeacherId()).getUserId();

            classesInfo.setTeacherName(usersRepository.findUsersByUserId(teacherUserId).getFullName());
            return classesInfo;
        }
        else {
            return null;
        }
    }

    public ClassesInfo getTeachersClassByClassId(int teacherId, int classId) {
        try {
            Classes classes = classesRepository.findClassesByClassIdAndTeacherId(classId, teacherId);
            ClassesInfo classesInfo = new ClassesInfo(classes);

            int teacherUserId = teacherRepository.findTeacherByTeacherId(teacherId).getUserId();
            classesInfo.setTeacherName(usersRepository.findUsersByUserId(teacherUserId).getFullName());
            return classesInfo;
        }
        catch(Exception e) {
            return null;
        }
    }

    public Boolean teacherIsClassOwner(int teacherId, int classId){
        return classesRepository.findClassesByClassIdAndTeacherId(classId, teacherId) != null;
    }

    public Classes getClassesByClassId(int classId) {
        return classesRepository.findClassesByClassId(classId);
    }
}

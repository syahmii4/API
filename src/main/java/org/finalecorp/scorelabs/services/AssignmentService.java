package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Assignment;
import org.finalecorp.scorelabs.models.Classes;
import org.finalecorp.scorelabs.repositories.AssignmentRepository;
import org.finalecorp.scorelabs.requestObjects.CreateAssignmentForm;
import org.finalecorp.scorelabs.requestObjects.EditAssignmentForm;
import org.finalecorp.scorelabs.responseObjects.AssignmentStream;
import org.finalecorp.scorelabs.responseObjects.ClassesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final ClassesService classesService;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, ClassesService classesService){
        this.assignmentRepository = assignmentRepository;
        this.classesService = classesService;
    }

    public String createAssignment(CreateAssignmentForm form) {
        Assignment assignment = new Assignment();
        assignment.setClassId(form.classId);
        assignment.setDescription(form.description);
        assignment.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        assignment.setDateDue(Timestamp.valueOf(form.dateDue));
        assignment.setAssignmentTitle(form.assignmentTitle);
        assignment.setAssignmentType(form.assignmentType);
        assignment.setAttempts(form.attempts);

        assignmentRepository.save(assignment);
        return "okieee";
    }

    public List<Assignment> getAssignmentByClass(int classId){
        return assignmentRepository.findAssignmentByClassId(classId);
    }

    public List<AssignmentStream> getAssignmentByStudent(int studentId){
        List<ClassesInfo> classes = classesService.getClassesByStudentId(studentId);

        List<Integer> classIds = new ArrayList<>();
        for (ClassesInfo classesObj : classes) {
            classIds.add(classesObj.getClassId());
        }

        List<Assignment> assignments= assignmentRepository.findAllAssignmentByClassIdIn(classIds);
        List<AssignmentStream> assignmentStreams = new ArrayList<>();
        for(Assignment assignment : assignments) {
            AssignmentStream stream = new AssignmentStream(assignment);
            String displayColor = classesService.getClassesByClassId(assignment.getClassId()).getDisplayColor();
            stream.setDisplayColor(displayColor);
            assignmentStreams.add(stream);
        }

        return assignmentStreams;
    }

    public String editAssignment(EditAssignmentForm form) {
        Assignment assignment = assignmentRepository.findAssignmentByAssignmentId(form.assignmentId);
        assignment.setDescription(form.description);
        assignment.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        assignment.setDateDue(Timestamp.valueOf(form.dateDue));
        assignment.setAssignmentTitle(form.assignmentTitle);
        assignment.setAssignmentType(form.assignmentType);
        assignment.setAttempts(form.attempts);

        assignmentRepository.save(assignment);
        return "okieee";
    }

    public void deleteAssignment(int assignmentId, int teacherId) {
        Assignment assignment = assignmentRepository.findAssignmentByAssignmentId(assignmentId);
        if(classesService.teacherIsClassOwner(teacherId, assignment.getClassId())){
            assignmentRepository.delete(assignment);
        }
    }

    public Assignment getAssignmentById(int assignmentId) {
        return assignmentRepository.findAssignmentByAssignmentId(assignmentId);
    }

    public void insertQuestions(Map<Object, Object> questions, int id) {
        Assignment assignment = assignmentRepository.findAssignmentByAssignmentId(id);
        assignment.setQuestion(questions);
    }
}

package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Classroom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends CrudRepository<Classroom, Integer> {
    public Classroom findClassroomByClassroomId(int classroomId);
    public List<Classroom> findClassroomByStudentId(int studentId);
}

package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    public Teacher findTeacherByTeacherId(int teacherId);

    public Teacher findTeacherByUserId(int userId);
}

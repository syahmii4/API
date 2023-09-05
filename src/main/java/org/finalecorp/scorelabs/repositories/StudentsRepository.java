package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Students;
import org.finalecorp.scorelabs.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsRepository extends CrudRepository<Students,Integer> {
    public Students findStudentsByStudentId(Integer studentId);

    Students findStudentsByUserId(int userId);
}

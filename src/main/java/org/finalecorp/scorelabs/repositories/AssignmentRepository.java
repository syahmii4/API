package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Assignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Integer> {

    List<Assignment> findAssignmentByClassId(int classId);

    Assignment findAssignmentByAssignmentId(int assignmentId);
}

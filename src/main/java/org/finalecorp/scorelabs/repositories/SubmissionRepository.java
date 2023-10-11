package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Submission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends CrudRepository<Submission, Integer> {

    List<Submission> findAllSubmissionsByAssignmentId(int assignmentId);

    List<Submission> findAllSubmissionsByAssignmentIdAndStudentId(int assignmentId, int studentId);
}

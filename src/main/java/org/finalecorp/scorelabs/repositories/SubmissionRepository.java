package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Submission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends CrudRepository<Submission, Integer> {

}

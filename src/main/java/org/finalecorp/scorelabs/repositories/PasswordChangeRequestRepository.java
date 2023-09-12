package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.PasswordChangeRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordChangeRequestRepository extends CrudRepository<PasswordChangeRequest, Integer> {
    PasswordChangeRequest findPasswordChangeRequestByPasscode(String passcode);

    PasswordChangeRequest findPasswordChangeRequestByUserId(int userId);
}

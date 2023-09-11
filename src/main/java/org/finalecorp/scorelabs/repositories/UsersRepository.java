package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Integer>{
    public Users findUsersByUsername(String username);

    public Users findUsersByUserId(int userId);
}
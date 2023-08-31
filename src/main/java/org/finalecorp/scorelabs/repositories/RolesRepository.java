package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends CrudRepository<Roles, Integer> {
    public Roles findRolesByRoleId(int roleId);
}

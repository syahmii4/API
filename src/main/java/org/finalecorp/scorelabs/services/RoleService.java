package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RolesRepository rolesRepository;

    @Autowired
    public RoleService(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    public String getRoleName(int roleId){
        return rolesRepository.findRolesByRoleId(roleId).getRoleName();
    }
}

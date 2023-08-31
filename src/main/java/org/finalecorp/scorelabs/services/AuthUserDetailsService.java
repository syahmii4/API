package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.RolesRepository;
import org.finalecorp.scorelabs.repositories.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    private final UsersRepository userRepository;
    private final RolesRepository rolesRepository;


    public AuthUserDetailsService(UsersRepository userRepository, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findUsersByUsername(username);
        String[] userRole = {rolesRepository.findRolesByRoleId(user.getRole()).getRoleName()};

        return User.builder().username(user.getUsername()).password(user.getPassword()).roles(userRole).build();
    }
}

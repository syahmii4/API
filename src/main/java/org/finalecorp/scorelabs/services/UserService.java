package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.UsersRepository;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public Users createUser(RegisterForm registerForm){
        Users newUser = new Users();
        Date date = new Date();
        newUser.setUsername(registerForm.username);
        newUser.setPassword(passwordEncoder.encode(registerForm.password));
        newUser.setFullName(registerForm.fullName);
        newUser.setRole(registerForm.role);
        newUser.setDateCreated(new Timestamp(date.getTime()));
        newUser = usersRepository.save(newUser);
        return newUser;
    }
}


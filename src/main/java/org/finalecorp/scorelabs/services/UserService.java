package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.RolesRepository;
import org.finalecorp.scorelabs.repositories.StudentsRepository;
import org.finalecorp.scorelabs.repositories.TeacherRepository;
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
    private final RolesRepository rolesRepository;

    private final StudentsService studentsService;

    private final TeacherService teacherService;
    private final ParentService parentService;

    @Autowired
    public UserService(UsersRepository usersRepository, RolesRepository rolesRepository, StudentsService studentsService, TeacherService teacherService, ParentService parentService) {

        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.studentsService = studentsService;
        this.teacherService = teacherService;
        this.parentService = parentService;
    }
    public Users createUser(RegisterForm registerForm){
        if(!this.userExists(registerForm.username)){
            Users newUser = new Users();
            Date date = new Date();
            newUser.setUsername(registerForm.username);
            newUser.setEmailAddress(registerForm.emailAddress);
            newUser.setPassword(passwordEncoder.encode(registerForm.password));
            newUser.setFullName(registerForm.fullName);
            newUser.setRole(registerForm.role);
            newUser.setDateCreated(new Timestamp(date.getTime()));
            newUser = usersRepository.save(newUser);

            int userId = usersRepository.findUsersByUsername(registerForm.username).getUserId();

            switch (registerForm.role) {
                case 1:
                    studentsService.createStudent(userId);
                    break;
                case 2:
                    teacherService.createTeacher(userId);
                    break;
                case 3:
                    parentService.createParent(userId);
                default:
                    return null;
            }
            return newUser;
        }
        return null;
    }

    public Users getUserByUsername(String username){
        return usersRepository.findUsersByUsername(username);
    }
    public boolean userExists(String username){
        return usersRepository.findUsersByUsername(username) != null;
    }
}
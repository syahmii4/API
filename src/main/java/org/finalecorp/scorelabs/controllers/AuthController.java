package org.finalecorp.scorelabs.controllers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.repositories.UsersRepository;
import org.finalecorp.scorelabs.requestObjects.LoginForm;
import org.finalecorp.scorelabs.responseObjects.ErrorResponse;
import org.finalecorp.scorelabs.responseObjects.LoginResponse;
import org.finalecorp.scorelabs.utilities.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;
    private final UsersRepository usersRepository;
    @Autowired
    private AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UsersRepository usersRepository){
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.usersRepository = usersRepository;
    }

    @ResponseBody
    @PostMapping("/rest/auth/login")
    public ResponseEntity login(@RequestBody LoginForm loginForm){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username, loginForm.password));
            String username = auth.getName();
            Users user = usersRepository.findUsersByUsername(username);
            String token = jwtTokenUtil.createToken(user);
            LoginResponse loginRes = new LoginResponse(username, token);

            return ResponseEntity.ok(loginRes);
        }
        catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

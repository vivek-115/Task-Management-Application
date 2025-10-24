package com.vivekdev.TaskApp.services.impl;

import com.vivekdev.TaskApp.dto.Response;
import com.vivekdev.TaskApp.dto.UserRequest;
import com.vivekdev.TaskApp.entity.User;
import com.vivekdev.TaskApp.enums.Role;
import com.vivekdev.TaskApp.exceptions.BadRequestException;
import com.vivekdev.TaskApp.exceptions.NotFoundException;
import com.vivekdev.TaskApp.repo.UserRepositry;
import com.vivekdev.TaskApp.security.JWTUtils;
import com.vivekdev.TaskApp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepositry userRepositry;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;


    @Override
    public Response<?> signUp(UserRequest userRequest) {
        log.info("INSIDE SIGNUP()");
        Optional<User> existingUser=userRepositry.findByUsername(userRequest.getUsername());
        if(existingUser.isPresent()){
            throw new BadRequestException("Username is already taken");
        }
        User user=new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRoles(Role.USER);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepositry.save(user);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("user registered successfully")
                .build();
    }

    @Override
    public Response<?> logIn(UserRequest userRequest) {
        log.info("INSIDE LogIn()");
       User user =userRepositry.findByUsername(userRequest.getUsername())
               .orElseThrow(()->new NotFoundException("User not Found"));

       if(!passwordEncoder.matches(userRequest.getPassword(),user.getPassword())){
           throw new BadRequestException("Invalid Password");
       }

       String token=jwtUtils.generateToken(user.getUsername());

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successfully")
                .data(token)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
       String username= SecurityContextHolder.getContext().getAuthentication().getName();
       return userRepositry.findByUsername(username).orElseThrow(()->new NotFoundException("User not found"));
    }
}

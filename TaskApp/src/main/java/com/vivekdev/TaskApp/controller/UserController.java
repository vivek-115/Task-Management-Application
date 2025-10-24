package com.vivekdev.TaskApp.controller;

import com.vivekdev.TaskApp.dto.Response;
import com.vivekdev.TaskApp.dto.UserRequest;
import com.vivekdev.TaskApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response<?>> signUp(@Valid  @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.signUp(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> login(@Valid  @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.logIn(userRequest));
    }
}

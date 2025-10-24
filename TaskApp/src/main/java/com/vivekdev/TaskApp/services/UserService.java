package com.vivekdev.TaskApp.services;

import com.vivekdev.TaskApp.dto.Response;
import com.vivekdev.TaskApp.dto.UserRequest;
import com.vivekdev.TaskApp.entity.User;

public interface UserService {
    Response<?> signUp(UserRequest userRequest);
    Response<?> logIn(UserRequest userRequest);

   User getCurrentLoggedInUser();
}

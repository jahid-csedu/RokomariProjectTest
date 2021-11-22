package com.example.demo.controller;

import com.example.demo.model.SignupRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public String addNewUser(@RequestBody SignupRequest request) {
        User user = new User();
        user.setUserName(request.getUsername());
        user.setPassword(request.getPassword());
        try {
            User result = userService.createUser(user, request.getRole());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "User Created successfully";
    }

}

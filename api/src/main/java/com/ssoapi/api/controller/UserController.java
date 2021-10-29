package com.ssoapi.api.controller;

import com.ssoapi.api.component.LoginUserHolder;
import com.ssoapi.api.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginUserHolder loginUserHolder;

    @GetMapping("/currentUser")
    public UserDto currentUser(){
        return loginUserHolder.getCurrentUser();
    }

}

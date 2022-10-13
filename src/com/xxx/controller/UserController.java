package com.xxx.controller;

import com.xxx.annotation.Autowired;
import com.xxx.annotation.Controller;
import com.xxx.service.UserService;

@Controller
public class UserController {
    @Autowired
    public UserService userService;
    public void findUser(){

        userService.findUser();
    }
}

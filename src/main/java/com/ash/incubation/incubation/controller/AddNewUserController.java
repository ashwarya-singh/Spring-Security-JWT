package com.ash.incubation.incubation.controller;

import com.ash.incubation.incubation.entity.UserInfo;
import com.ash.incubation.incubation.service.BasicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AddNewUserController {

    @Autowired
    BasicSecurityService service;

    @PostMapping("/adduser")
    public String addUser(@RequestBody UserInfo userInfo){
        return service.addUser(userInfo);
    }
}

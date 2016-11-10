package com.csdn.controller;

import com.csdn.model.User;
import com.csdn.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zbin on 16-11-9.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/get")
    public @ResponseBody User getById(int uid) {
        return userService.getById(uid);
    }
}

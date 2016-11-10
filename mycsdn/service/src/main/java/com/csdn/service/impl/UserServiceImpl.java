package com.csdn.service.impl;

import com.csdn.mapper.UserMapper;
import com.csdn.model.User;
import com.csdn.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zbin on 16-11-9.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User getById(int uid) {
        return userMapper.getById(uid);
    }
}

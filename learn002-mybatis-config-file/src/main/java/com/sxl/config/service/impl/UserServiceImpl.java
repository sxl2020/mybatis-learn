package com.sxl.config.service.impl;


import com.sxl.config.service.UserService;
import com.sxl.config.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sxl.config.bean.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserInfo(String userName) {
        return userMapper.selectUserInfo(userName);
    }

}
package com.sxl.config.service;

import com.sxl.config.bean.User;

public interface UserService {

    User selectUserInfo(String userName);

}

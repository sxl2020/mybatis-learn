package com.sxl.config.mapper;

import com.sxl.config.bean.User;

public interface UserMapper {
    User selectUserInfo(String userName);
}

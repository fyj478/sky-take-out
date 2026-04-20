package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
/**
 * 用户登录
 */
public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}

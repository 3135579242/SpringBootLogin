package com.along.study.service;

import com.along.study.domain.dto.UserRegisterDTO;
import com.along.study.domain.entity.User;
import com.along.study.domain.responseresult.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO);

    User findAccountByUsername(String username);

}

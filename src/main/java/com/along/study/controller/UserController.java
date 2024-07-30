package com.along.study.controller;

import com.along.study.domain.dto.UserRegisterDTO;
import com.along.study.domain.responseresult.ResponseResult;
import com.along.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 时间 2024年07月29日
 */


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseResult<Void> userRegister(@RequestBody UserRegisterDTO userRegisterDTO){
        return userService.userRegister(userRegisterDTO);
    }

    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @GetMapping("/test1")
    public ResponseResult<String> test1(){
        return ResponseResult.success("test1 带权限的");
    }

    @GetMapping("/test2")
    public ResponseResult<String> test2(){
        return ResponseResult.success("test2 不带权限的");
    }



}

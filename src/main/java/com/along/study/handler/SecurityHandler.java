package com.along.study.handler;

import com.along.study.domain.entity.LoginUser;
import com.along.study.domain.entity.User;
import com.along.study.domain.responseresult.ResponseResult;
import com.along.study.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.UUID;

/**
 * 时间 2024年07月29日
 */
@Component
public class SecurityHandler {

    @Autowired
    private JwtUtils jwtUtils;

    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        handlerOnAuthenticationSuccess(request,response,(LoginUser)authentication.getPrincipal());
    }

    public void handlerOnAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            LoginUser loginUser
    ){
        Long id =  loginUser.getUser().getId();
        String username =  loginUser.getUser().getUsername();
        String uuid = UUID.randomUUID().toString();
        String jwt = jwtUtils.createJwt(uuid, loginUser, id, username);
        this.render(response,jwt);
    }

    public void render(HttpServletResponse response,String jwt){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.setStatus(200);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(ResponseResult.success(jwt,"登录成功").asJsonString());
        }catch (Exception e){

        }
    }

}

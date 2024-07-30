package com.along.study.service.impl;

import com.along.study.domain.dto.UserRegisterDTO;
import com.along.study.domain.entity.*;
import com.along.study.domain.responseresult.ResponseResult;
import com.along.study.enums.RespEnum;
import com.along.study.mapper.*;
import com.along.study.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 时间 2024年07月29日
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findAccountByUsername(username);
        //LoginUser handlerLogin();
        return this.handlerLogin(user);
    }

    /**
     * 查询用户
     *
     * @param username
     * @return
     */
    @Override
    public User findAccountByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException(RespEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
        }
        return user;
    }

    public LoginUser handlerLogin(User user) {
        //权限
        //用户角色关联
        LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(UserRole::getUserId, user.getId());
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
        //角色
        List<Role> roleList = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            roleList.add(role);
        }
        if (!roleList.isEmpty()) {
            //角色权限关联表
            List<RolePermission> rolePermissions = new ArrayList<>();
            for (Role role : roleList) {
                LambdaQueryWrapper<RolePermission> rolePermissionWrapper = new LambdaQueryWrapper<>();
                rolePermissionWrapper.eq(RolePermission::getRoleId, role.getId());
                rolePermissions = rolePermissionMapper.selectList(rolePermissionWrapper);
            }
            //权限
            Set<Permission> permissionSet = new HashSet<>();
            for (RolePermission rolePermission : rolePermissions) {
                Permission permission = permissionMapper.selectById(rolePermission.getPermissionId());
                permissionSet.add(permission);
            }
            permissionSet.forEach(System.out::println);
            Set<String> permissionKey = new HashSet<>();
            for (Permission permission : permissionSet) {
                permissionKey = Set.of(permission.getPermissionKey());
            }
            return new LoginUser(user, permissionKey.stream().toList());
        }else {
            return new LoginUser(user, List.of());
        }




    }

    @Override
    public ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO) {
        //用户名最短为 1 最长为 10
        if (!(userRegisterDTO.getUsername().length() >= 1 && userRegisterDTO.getUsername().length() <= 10)) {
            //长度异常
            return ResponseResult.failure("用户名最短为 1 最长为 10");
        }
        //用户密码最短为 6 最长为 20
        if (!(userRegisterDTO.getPassword().length() >= 6 && userRegisterDTO.getPassword().length() <= 20)) {
            //长度异常
            return ResponseResult.failure("用户密码最短为 6 最长为 20");
        }
        //判断用户名是否存在
        if (this.userIsExist(userRegisterDTO.getUsername())) {
            //用户存在异常
            return ResponseResult.failure("用户存在异常");
        }
        Date date = new Date();
        User user = User.builder()
                .id(null)
                .nickname(userRegisterDTO.getUsername())
                .username(userRegisterDTO.getUsername())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .gender(0)
                .avatar("http://xxxx.com/avatar.png")
                // TODO Ip地址待完善
                .registerIp("127.0.0.1")
                .registerType(0)
                .registerAddress("内网地址")

                .isDisable(0)
                .loginTime(date)
                .isDeleted(0)
                .build();
        if (this.save(user)) {
            return ResponseResult.success();
        } else {
            return ResponseResult.failure();
        }
    }

    /**
     * 判断用户是否存在
     *
     * @param username
     * @return
     */
    private boolean userIsExist(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.userMapper.selectOne(wrapper) != null;
    }


}

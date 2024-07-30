package com.along.study.utils;

import com.along.study.domain.entity.*;
import com.along.study.mapper.PermissionMapper;
import com.along.study.mapper.RoleMapper;
import com.along.study.mapper.RolePermissionMapper;
import com.along.study.mapper.UserRoleMapper;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 时间 2024年07月29日
 */

@Component
public class JwtUtils {

    @Autowired
    private RedisCache redisCache;

    /**
     * 创建 jwt
     *
     * @param details  用户信息
     * @param id       用户id
     * @param username 用户名
     * @return String jwt
     */
    public String createJwt(String uuid, UserDetails details, Long id, String username) {
        Algorithm algorithm = Algorithm.HMAC256("jwt:key");
        Date expire = expireTime();
        // 当前时间
        Date now = new Date();
        String jwt = JWT.create().withJWTId(uuid).withClaim("id", id).withClaim("name", username)
//                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire).withIssuedAt(now).sign(algorithm);
        // 存入redis
        redisCache.setCacheObject("jwt:login" + uuid, jwt, (int) (expire.getTime() - now.getTime()), TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 解析jwt
     *
     * @param headerToken 请求头中的token
     * @return {@link DecodedJWT} 解析后的jwt
     */
    public DecodedJWT resolveJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256("jwt:key");
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            // 是否合法，不合法会抛出一个运行时异常（需要自己捕获）
            DecodedJWT verify = jwtVerifier.verify(token);
            // 如果不在白名单中
            if (this.isInvalidToken(verify.getId())) return null;
            Date expiresAt = verify.getExpiresAt();
            // 判断是否过期
            return new Date().after(expiresAt) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    /**
     * 是否是一个过期的令牌
     *
     * @param uuid 令牌的id
     * @return boolean
     */
    private boolean isInvalidToken(String uuid) {
        // 判断是否在redis中(白名单)
        return !Boolean.TRUE.equals(redisCache.isHasKey("jwt:login" + uuid));
    }

    /**
     * 处理Token
     *
     * @param headerToken 请求头中的token
     * @return String
     */
    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer ")) return null;
        return headerToken.substring(7);
    }


    /**
     * 到期时间
     *
     * @return Date
     */
    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 7 * 24);
        return calendar.getTime();
    }


    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    public UserDetails toUser(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        //权限
        //用户角色关联
        LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(UserRole::getUserId, claims.get("id").asInt());
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);

        //角色
        List<Role> roleSet = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            roleSet.add(role);
        }
        //角色权限关联表
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Role role : roleSet) {
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
//        User user = new User();
//        user.setUsername(claims.get("name").asString());
//        return new LoginUser(user,permissionKey.stream().toList());
        return LoginUser.builder()
                .user(
                        User.builder()
                                .username(claims.get("name").asString())
                                .build()
                )
                .permissions(permissionKey.stream().toList())
                .build();
    }
}

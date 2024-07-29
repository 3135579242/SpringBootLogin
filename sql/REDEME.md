
# 创建5张表
- 用户表  ``user``
- 用户角色关联表 ``user_role``
- 角色表 ``role``
- 角色权限关联表 ``role_permission``
- 权限表 ``permission``

1. 用户表  ``sys_user``
```sql
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户昵称',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户密码',
  `gender` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户性别(0未定义,1男,2女)',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户头像',
  `intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户个人简介',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户邮箱',
  `register_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '注册ip',
  `register_type` tinyint NOT NULL COMMENT '注册方式(0邮箱/姓名 1Gitee 2Github)',
  `register_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '注册地址',
  `login_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最近登录ip',
  `login_type` tinyint NULL DEFAULT NULL COMMENT '最近登录类型(0邮箱/姓名 1Gitee 2Github)',
  `login_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最近登录地址',
  `login_time` datetime NOT NULL COMMENT '用户最近登录时间',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;
```

























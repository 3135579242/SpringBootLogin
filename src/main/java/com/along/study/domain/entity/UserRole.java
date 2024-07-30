package com.along.study.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 时间 2024年07月30日
 */
@TableName("sys_user_role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    //id
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Integer userId;
    //角色id
    private Integer roleId;

}

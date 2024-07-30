package com.along.study.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 时间 2024年07月30日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_role")
public class Role {

    @TableId(type = IdType.AUTO)
    private Long id;
    //角色名称
    private String roleName;
    //角色标识符
    private String roleKey;
    //状态
    private Integer status;
    //排序
    private Integer orderNum;
    //备注
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer isDeleted;

}

package com.along.study.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_permission")
public class Permission {

    private Long id;
    //权限描述
    private String permissionDesc;
    //权限标识符
    private String permissionKey;
    //菜单id
    private Integer menuId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer isDeleted;

}

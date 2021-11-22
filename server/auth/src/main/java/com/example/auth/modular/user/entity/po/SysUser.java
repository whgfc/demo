package com.example.auth.modular.user.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.example.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author demo
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private Long avatar;

    /**
     * 生日
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private Date birthday;

    /**
     * 性别(字典 1男 2女 3未知)
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 电话
     */
    private String tel;

    /**
     * 最后登陆IP
     */
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    private Date lastLoginTime;

    /**
     * 管理员类型（1超级管理员 2非管理员）
     */
    private Integer adminType;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;

    /**
     * 是否开启单用户登录
     */
    private Boolean singleLogin;
}

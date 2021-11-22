package com.example.common.login;

import cn.hutool.core.lang.Dict;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysLoginUser {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

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
    private String avatar;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别(字典 1男 2女)
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
     * 管理员类型（0超级管理员 1非管理员）
     */
    private Integer adminType;

    /**
     * 最后登陆IP
     */
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    private String lastLoginTime;

    /**
     * 最后登陆地址
     */
    private String lastLoginAddress;

    /**
     * 最后登陆所用浏览器
     */
    private String lastLoginBrowser;

    /**
     * 最后登陆所用系统
     */
    private String lastLoginOs;

    /**
     * 员工信息
     */
    //private LoginEmpInfo loginEmpInfo;

    /**
     * 具备应用信息
     */
    private List<Dict> apps;

    /**
     * 角色信息
     */
    private List<Dict> roles;

    /**
     * 权限信息
     */
    private List<String> permissions;

    /**
     * 登录菜单信息，AntDesign版本菜单
     */
    //private List<LoginMenuTreeNode> menus;

    /**
     * 数据范围信息
     */
    private List<Long> dataScopes;

    /**
     * 租户信息
     */
    private Dict tenants;

}

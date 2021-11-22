package com.example.auth.modular.user.controller;

import com.example.auth.modular.user.entity.vo.SysUserParam;
import com.example.auth.modular.user.service.ISysUserService;
import com.example.common.pojo.response.CommonResult;
import com.example.common.pojo.response.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;

    @PostMapping
    public CommonResult<String> add (@RequestBody SysUserParam sysUserParam) {
        sysUserService.add(sysUserParam);
        return new SuccessResult<>();
    }

}

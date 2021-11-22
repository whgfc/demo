package com.example.auth.modular.auth.controller;

import com.example.auth.modular.auth.entity.param.LoginParam;
import com.example.auth.modular.auth.service.IAuthService;
import com.example.common.pojo.response.CommonResult;
import com.example.common.pojo.response.SuccessResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IAuthService authService;

    @PostMapping("/login")
    public CommonResult<String> login(@RequestBody LoginParam param) {
        String token = authService.doLogin(param.getAccount(), param.getPassword());
        return new SuccessResult<>(token);
    }

    @GetMapping("/logout")
    public CommonResult<String> logout() {
        authService.logout();
        return new SuccessResult<>();
    }
}

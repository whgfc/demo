package com.example.auth.modular.auth.entity.param;

import lombok.Data;

@Data
public class LoginParam {
    private String account;
    private String password;
    private Integer loginType;
    private String tenantCode;
}

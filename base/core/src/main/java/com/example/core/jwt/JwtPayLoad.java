package com.example.core.jwt;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

/**
 * @author demo
 * @description jwt荷载数据包装类
 */
@Data
public class JwtPayLoad {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 唯一表示id, 用于缓存登录用户的唯一凭证
     */
    private String uuid;

    public JwtPayLoad() {
    }

    public JwtPayLoad(Long userId, String account) {
        this.userId = userId;
        this.account = account;
        this.uuid = IdUtil.fastUUID();
    }

}

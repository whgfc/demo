package com.example.auth.core.log.factory;

import cn.hutool.core.date.DateTime;
import com.example.auth.core.log.enums.LogSuccessStatusEnum;
import com.example.auth.core.log.enums.VisLogTypeEnum;
import com.example.auth.modular.log.entity.SysVisLog;
import com.example.common.consts.SymbolConstant;

public class LogFactory {

    /**
     * 创建登录日志
     */
    static void createSysLoginLog(SysVisLog sysVisLog, String account, String successCode, String failMessage) {
        sysVisLog.setName(VisLogTypeEnum.LOGIN.getMessage());
        sysVisLog.setSuccess(successCode);

        sysVisLog.setVisType(VisLogTypeEnum.LOGIN.getCode());
        sysVisLog.setVisTime(DateTime.now());
        sysVisLog.setAccount(account);

        if (LogSuccessStatusEnum.SUCCESS.getCode().equals(successCode)) {
            sysVisLog.setMessage(VisLogTypeEnum.LOGIN.getMessage() + LogSuccessStatusEnum.SUCCESS.getMessage());
        }
        if (LogSuccessStatusEnum.FAIL.getCode().equals(successCode)) {
            sysVisLog.setMessage(VisLogTypeEnum.LOGIN.getMessage() +
                    LogSuccessStatusEnum.FAIL.getMessage() + SymbolConstant.COLON + failMessage);
        }
    }

    /**
     * 创建登出日志
     */
    static void createSysExitLog(SysVisLog sysVisLog, String account) {
        sysVisLog.setName(VisLogTypeEnum.EXIT.getMessage());
        sysVisLog.setSuccess(LogSuccessStatusEnum.SUCCESS.getCode());
        sysVisLog.setMessage(VisLogTypeEnum.EXIT.getMessage() + LogSuccessStatusEnum.SUCCESS.getMessage());
        sysVisLog.setVisType(VisLogTypeEnum.EXIT.getCode());
        sysVisLog.setVisTime(DateTime.now());
        sysVisLog.setAccount(account);
    }

    /**
     * 构建基础访问日志
     */
    public static SysVisLog genBaseSysVisLog(String ip, String location, String browser, String os) {
        SysVisLog sysVisLog = new SysVisLog();
        sysVisLog.setIp(ip);
        sysVisLog.setLocation(location);
        sysVisLog.setBrowser(browser);
        sysVisLog.setOs(os);
        return sysVisLog;
    }

}

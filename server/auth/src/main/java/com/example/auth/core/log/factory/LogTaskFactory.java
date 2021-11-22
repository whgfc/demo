package com.example.auth.core.log.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.example.auth.modular.log.entity.SysVisLog;
import com.example.auth.modular.log.service.ISysVisLogService;
import com.example.common.context.requestNo.RequestNoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class LogTaskFactory {

    private static final Logger logger = LoggerFactory.getLogger(LogTaskFactory.class);

    private static final ISysVisLogService sysVisLogService = SpringUtil.getBean(ISysVisLogService.class);

    public static TimerTask loginLog(SysVisLog sysVisLog, final String account, String success, String failMessage) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysLoginLog(sysVisLog, account, success, failMessage);
                    sysVisLogService.save(sysVisLog);
                } catch (Exception e) {
                    logger.error(">>> 创建登录日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }

    /**
     * 登出日志
     */
    public static TimerTask exitLog(SysVisLog sysVisLog, String account) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysExitLog(sysVisLog, account);
                    sysVisLogService.save(sysVisLog);
                } catch (Exception e) {
                    logger.error(">>> 创建退出日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }

}

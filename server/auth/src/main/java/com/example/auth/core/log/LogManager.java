package com.example.auth.core.log;

import cn.hutool.core.util.ObjectUtil;
import com.example.auth.core.log.factory.LogFactory;
import com.example.auth.core.log.factory.LogTaskFactory;
import com.example.auth.modular.log.entity.SysVisLog;
import com.example.common.exception.ServiceException;
import com.example.common.exception.enums.AuthExceptionEnum;
import com.example.common.exception.enums.ServerExceptionEnum;
import com.example.core.utils.HttpServletUtil;
import com.example.core.utils.IpAddressUtil;
import com.example.core.utils.UaUtil;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogManager {

    /**
     * 异步记录日志线程池
     */
    private static final ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(6, new ScheduledExecutorFactoryBean());

    private LogManager() {}

    private static final LogManager LOG_MANAGER = new LogManager();

    public static LogManager me() {
        return LOG_MANAGER;
    }

    /**
     * 登录日志
     */
    public void executeLoginLog(String account, String success, String failMessage) {
        SysVisLog sysVisLog = genSysVisLog();
        TimerTask timerTask = LogTaskFactory.loginLog(sysVisLog, account,
                success,
                failMessage);

        executeLog(timerTask);
    }

    /**
     * 异步执行日志的方法
     */
    private void executeLog(TimerTask timerTask){
        //日志记录操作延时
        int operateDelayTime = 10;
        executorService.schedule(timerTask, operateDelayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 登出日志
     */
    public void executeExitLog(final String account) {
        SysVisLog sysVisLog = this.genSysVisLog();
        TimerTask timerTask = LogTaskFactory.exitLog(sysVisLog, account);
        executeLog(timerTask);
    }


    /**
     * 构建基础访问日志
     */
    private SysVisLog genSysVisLog() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (ObjectUtil.isNotNull(request)) {
            String ip = IpAddressUtil.getIp(request);
            String address = IpAddressUtil.getAddress(request);
            String browser = UaUtil.getBrowser(request);
            String os = UaUtil.getOs(request);
            return LogFactory.genBaseSysVisLog(ip, address, browser, os);
        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }

}

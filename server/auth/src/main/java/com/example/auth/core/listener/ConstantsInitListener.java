package com.example.auth.core.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.example.auth.modular.consts.enums.SysConfigExceptionEnum;
import com.example.common.consts.CommonConstant;
import com.example.common.context.constant.ConstantContext;
import com.example.common.enums.CommonStatusEnum;
import com.example.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @author demo
 * @description
 */
@Component
public class ConstantsInitListener implements ApplicationListener<ApplicationContextInitializedEvent>, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ConstantsInitListener.class);

    private static final String CONFIG_LIST_SQL = "select code,value from sys_config where status = ?";

    private static final String CAPITAL_CODE = "CODE";

    private static final String CODE = "code";

    private static final String CAPITAL_VALUE = "VALUE";

    private static final String VALUE = "value";

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");

        // 缓存中放入datasource链接，代码生成时候使用
        ConstantContext.putConstant(CommonConstant.DATABASE_URL_NAME, dataSourceUrl);
        ConstantContext.putConstant(CommonConstant.DATABASE_DRIVER_NAME, environment.getProperty("spring.datasource.driver-class-name"));
        ConstantContext.putConstant(CommonConstant.DATABASE_USER_NAME, dataSourceUsername);


        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername, dataSourcePassword)) {
            throw new ServiceException(SysConfigExceptionEnum.DATA_SOURCE_NOT_EXIST);
        }

        Connection conn = null;
        try {
            Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
            assert dataSourceUrl != null;
            conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);

            // 获取sys_config表的数据
            List<Entity> entityList = SqlExecutor.query(conn, CONFIG_LIST_SQL, new EntityListHandler(), CommonStatusEnum.ENABLE.getCode());

            // 将查询到的参数配置添加到缓存
            if (ObjectUtil.isNotEmpty(entityList)) {
                entityList.forEach(sysConfig ->
                        ConstantContext.putConstant(
                                sysConfig.getStr(CODE) == null ? sysConfig.getStr(CAPITAL_CODE) : sysConfig.getStr(CODE),
                                sysConfig.getStr(VALUE) == null ? sysConfig.getStr(CAPITAL_VALUE) : sysConfig.getStr(VALUE)
                        )
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error(">>> 读取数据库constants配置信息出错：");
            e.printStackTrace();
            throw new ServiceException(SysConfigExceptionEnum.DATA_SOURCE_NOT_EXIST);
        } finally {
            DbUtil.close(conn);
        }
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

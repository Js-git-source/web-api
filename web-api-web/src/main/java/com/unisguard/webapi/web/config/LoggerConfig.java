package com.unisguard.webapi.web.config;

import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.config.exception.GlobalExceptionHandler;
import com.unisguard.webapi.controller.api.ApiController;
import com.unisguard.webapi.controller.apiclient.ApiClientController;
import com.unisguard.webapi.controller.audit.AuditController;
import com.unisguard.webapi.controller.deploy.DeployController;
import com.unisguard.webapi.controller.group.GroupController;
import com.unisguard.webapi.controller.lic.LicController;
import com.unisguard.webapi.controller.menu.MenuController;
import com.unisguard.webapi.controller.mon.MonController;
import com.unisguard.webapi.controller.mon.MonHisController;
import com.unisguard.webapi.controller.role.RoleController;
import com.unisguard.webapi.controller.sysconfig.SysConfigController;
import com.unisguard.webapi.controller.upgrade.UpgradeController;
import com.unisguard.webapi.controller.user.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zemel
 * @date 2022/1/8 13:04
 */
@Configuration
public class LoggerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        setLoggerMap(event.getApplicationContext());
    }

    /**
     * 日志模块
     *
     * @param context
     */
    private void setLoggerMap(ApplicationContext context) {
        GlobalExceptionHandler globalExceptionHandler = context.getBean(GlobalExceptionHandler.class);
        Map<Logger, List<Class<?>>> loggerMap = globalExceptionHandler.getLoggerMap();
        // 系统管理日志
        loggerMap.put(LoggerFactory.getLogger(LogConstant.SYS), new ArrayList<Class<?>>() {
            {
                add(UserController.class);
                add(MenuController.class);
                add(RoleController.class);
                add(GroupController.class);
                add(ApiController.class);
                add(LicController.class);
                add(MonController.class);
                add(MonHisController.class);
                add(DeployController.class);
                add(ApiClientController.class);
                add(SysConfigController.class);
                add(AuditController.class);
                add(UpgradeController.class);
            }
        });
    }
}

package com.unisguard.webapi.service.init;

import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import com.unisguard.webapi.common.util.LicUtil;
import com.unisguard.webapi.manage.config.ConfigManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class LicInitService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(LogConstant.SYS);

    @Resource
    private ConfigManage configManage;

    @Override
    public void afterPropertiesSet() throws Exception {
        LicDO lic = configManage.findLicense();
        if (lic == null) {
            return;
        }
        try {
            GlobalConstant.LIC = LicUtil.checkLic(lic.getSecretKey());
        } catch (Exception e) {
            log.warn("系统授权初始化失败：", e);
        }
    }
}

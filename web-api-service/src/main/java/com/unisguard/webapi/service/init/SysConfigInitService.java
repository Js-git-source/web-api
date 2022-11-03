package com.unisguard.webapi.service.init;

import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.common.util.SysConfigUtil;
import com.unisguard.webapi.manage.sysconfig.SysConfigManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangzemo
 * @date 2022/3/17 14:40
 */
@Service
public class SysConfigInitService implements InitializingBean {
    @Resource
    private SysConfigManage sysConfigManage;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SysConfigDO> sysConfigDOList = sysConfigManage.queryAll();
        SysConfigUtil.initFirstLogin(sysConfigDOList);
    }
}

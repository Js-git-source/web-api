package com.unisguard.webapi.service.init;

import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.deploy.DeployDO;
import com.unisguard.webapi.manage.deploy.DeployManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.Inet4Address;


@Service
public class DeployInitService implements InitializingBean {
    @Resource
    private DeployManage deployManage;

    @Override
    public void afterPropertiesSet() throws Exception {
        DeployDO deployDO = new DeployDO();
        deployDO.setCategory(DictConstant.CORE_COMPONENT);
        deployDO.setType(DictConstant.ENGINE_TYPE_WEB);
        deployDO.setIp(Inet4Address.getLocalHost().getHostAddress());
        DeployDO oldDeployDO = deployManage.checkExist(deployDO);
        if (oldDeployDO == null) {
            deployManage.add(deployDO);
        }
    }
}

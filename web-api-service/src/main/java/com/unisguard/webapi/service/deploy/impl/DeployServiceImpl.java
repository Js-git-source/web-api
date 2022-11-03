package com.unisguard.webapi.service.deploy.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.deploy.DeployDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.manage.deploy.DeployManage;
import com.unisguard.webapi.service.deploy.DeployService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeployServiceImpl implements DeployService {
    @Resource
    private DeployManage deployManage;

    @Override
    public ResponseDO<List<DeployDO>> list(DeployDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<DeployDO> deployDOList = deployManage.list(param);
        return ResponseDO.success(deployDOList, page.getTotal());
    }

    @Override
    public ResponseDO<List<DeployDO>> monList(DeployDO param) {
        List<DeployDO> deployDOList = deployManage.list(param);
        return ResponseDO.success(deployDOList);
    }

    @Override
    public ResponseDO<Long> add(DeployDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.ENGINE_TYPE_WEB);
        //查询ip Category Type是否已经存在
        DeployDO deployDO = deployManage.checkExist(param);
        if (deployDO != null) {
            throw new ApplicationException("ip已经存在");
        }
        deployManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        deployManage.delete(id);
        return ResponseDO.success();
    }
}
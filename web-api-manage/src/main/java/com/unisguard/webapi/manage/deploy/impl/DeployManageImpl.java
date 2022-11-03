package com.unisguard.webapi.manage.deploy.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.deploy.DeployDO;
import com.unisguard.webapi.manage.deploy.DeployManage;
import com.unisguard.webapi.mapper.deploy.DeployMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class DeployManageImpl implements DeployManage {
    @Resource
    private DeployMapper deployMapper;

    @Override
    public List<DeployDO> list(DeployDO param) {
        return deployMapper.list(param);
    }

    @Override
    public void add(DeployDO param) {
        deployMapper.add(param);
    }

    @Override
    public void delete(Long id) {
        deployMapper.delete(id);
    }

    @Override
    public DeployDO checkExist(DeployDO param) {
        return deployMapper.checkExist(param);
    }
}
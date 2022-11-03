package com.unisguard.webapi.manage.api.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.manage.api.ApiManage;
import com.unisguard.webapi.mapper.api.ApiMapper;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author sunsaike
 * @description api管理Mansge
 */
@Manage
public class ApiManageImpl implements ApiManage {
    @Resource
    private ApiMapper apiMapper;

    @Override
    public List<ApiDO> list(ApiDO param) {
        return apiMapper.list(param);
    }

    @Override
    public void add(ApiDO param) {
        apiMapper.add(param);
    }

    @Override
    public ApiDO detail(Long id) {
        return apiMapper.detail(id);
    }

    @Override
    public void edit(ApiDO param) {
        apiMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        apiMapper.delete(id);
    }

    @Override
    public Integer exist(ApiDO apiDO) {
        return apiMapper.exist(apiDO);
    }
}
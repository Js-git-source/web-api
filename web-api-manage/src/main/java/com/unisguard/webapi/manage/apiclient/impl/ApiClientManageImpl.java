package com.unisguard.webapi.manage.apiclient.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.apiclient.ApiClientDO;
import com.unisguard.webapi.manage.apiclient.ApiClientManage;
import com.unisguard.webapi.mapper.apiclient.ApiClientMapper;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author sunsaike
 * @description api客户端Mannge
 */
@Manage
public class ApiClientManageImpl implements ApiClientManage {
    @Resource
    private ApiClientMapper apiClientMapper;

    @Override
    public List<ApiClientDO> list(ApiClientDO param) {
        return apiClientMapper.list(param);
    }

    @Override
    public void add(ApiClientDO param) {
        apiClientMapper.add(param);
    }

    @Override
    public ApiClientDO detail(Long id) {
        return apiClientMapper.detail(id);
    }

    @Override
    public void edit(ApiClientDO param) {
        apiClientMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        apiClientMapper.delete(id);
    }

    @Override
    public Boolean editStatus(ApiClientDO param) {
        return apiClientMapper.editStatus(param);
    }

    @Override
    public String getMaxAppId() {
        return apiClientMapper.getMaxAppId();
    }

    @Override
    public Integer exist(ApiClientDO apiClientDO) {
        return apiClientMapper.exist(apiClientDO);
    }
}
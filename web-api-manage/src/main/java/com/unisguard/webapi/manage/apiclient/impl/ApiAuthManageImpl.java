package com.unisguard.webapi.manage.apiclient.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.manage.apiclient.ApiAuthManage;
import com.unisguard.webapi.mapper.apiclient.ApiAuthMapper;

import javax.annotation.Resource;
import java.util.List;


/**
 * @description api授权Mansge
 * @author sunsaike
 */
@Manage
public class ApiAuthManageImpl implements ApiAuthManage {
    @Resource
    private ApiAuthMapper apiAuthMapper;

    @Override
    public List<ApiAuthDO> list(ApiAuthDO param) {
        return apiAuthMapper.list(param);
    }

    @Override
    public List<ApiDO> apiList(ApiAuthDO param) {
        return apiAuthMapper.apiList(param);
    }

    @Override
    public List<ApiDO> notApiList(ApiAuthDO param) {
        return apiAuthMapper.notApiList(param);
    }

    @Override
    public void add(ApiAuthDO param) {
        apiAuthMapper.add(param);
    }

    @Override
    public ApiAuthDO detail(Long id) {
        return apiAuthMapper.detail(id);
    }

    @Override
    public void edit(ApiAuthDO param) {
        apiAuthMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        apiAuthMapper.delete(id);
    }

    @Override
    public void cancelAuth(Long id) {
        apiAuthMapper.cancelAuth(id);
    }

}
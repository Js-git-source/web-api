package com.unisguard.webapi.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.ValidatorUtil;
import com.unisguard.webapi.manage.api.ApiManage;
import com.unisguard.webapi.manage.apiclient.ApiAuthManage;
import com.unisguard.webapi.service.api.ApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunsaike
 * @description api管理service
 */
@Service
public class ApiServiceImpl implements ApiService {
    @Resource
    private ApiManage apiManage;
    @Resource
    private ApiAuthManage apiAuthManage;

    @Override
    public ResponseDO<List<ApiDO>> list(ApiDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<ApiDO> apiDOList = apiManage.list(param);
        return ResponseDO.success(apiDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(ApiDO param) {
        if (!ValidatorUtil.isUsername(param.getName())) {
            throw new ApplicationException("接口名称只能包含中文和字母");
        }
        // 校验名字是否已经存在
        ApiDO apiDO = new ApiDO();
        apiDO.setName(param.getName());
        Integer exist = apiManage.exist(apiDO);
        if (exist > 0) {
            throw new ApplicationException("接口名称已存在");
        }
        ApiDO apiDoNew = new ApiDO();
        apiDoNew.setUrl(param.getUrl());
        Integer exist2 = apiManage.exist(apiDoNew);
        if (exist2 > 0) {
            throw new ApplicationException("接口URL已存在");
        }
        apiManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<ApiDO> detail(Long id) {
        ApiDO apiDO = apiManage.detail(id);
        return ResponseDO.success(apiDO);
    }

    @Override
    public ResponseDO<Long> edit(ApiDO param) {
        if (!ValidatorUtil.isUsername(param.getName())) {
            throw new ApplicationException("接口名称只能包含中文和字母");
        }
        ApiDO detail = apiManage.detail(param.getId());
        // 编辑时，如果名称发生了改变则需要校验名字是否已经存在
        if (!detail.getName().equals(param.getName())) {
            ApiDO apiDO = new ApiDO();
            apiDO.setName(param.getName());
            Integer exist = apiManage.exist(apiDO);
            if (exist > 0) {
                throw new ApplicationException("接口名称已存在");
            }
        }
        // 编辑时，如果名称发生了改变则需要校验名字是否已经存在
        if (!detail.getUrl().equals(param.getUrl())) {
            ApiDO apiDO = new ApiDO();
            apiDO.setUrl(param.getUrl());
            Integer exist = apiManage.exist(apiDO);
            if (exist > 0) {
                throw new ApplicationException("接口URL已存在");
            }
        }
        apiManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        ApiAuthDO apiAuthDO = new ApiAuthDO();
        apiAuthDO.setApiId(id);
        // 取消此接口所有已授权的数据
        apiAuthManage.cancelAuth(id);
        apiManage.delete(id);
        return ResponseDO.success();
    }
}
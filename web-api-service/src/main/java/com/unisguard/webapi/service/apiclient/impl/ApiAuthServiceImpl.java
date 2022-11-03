package com.unisguard.webapi.service.apiclient.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.manage.apiclient.ApiAuthManage;
import com.unisguard.webapi.service.apiclient.ApiAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiAuthServiceImpl implements ApiAuthService {
    @Resource
    private ApiAuthManage apiAuthManage;

    @Override
    public ResponseDO<List<ApiAuthDO>> list(ApiAuthDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<ApiAuthDO> apiAuthDOList = apiAuthManage.list(param);
        return ResponseDO.success(apiAuthDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(ApiAuthDO param) {
        apiAuthManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<ApiAuthDO> detail(Long id) {
        ApiAuthDO apiAuthDO = apiAuthManage.detail(id);
        return ResponseDO.success(apiAuthDO);
    }

    @Override
    public ResponseDO<Long> edit(ApiAuthDO param) {
        apiAuthManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        apiAuthManage.delete(id);
        return ResponseDO.success();
    }
}
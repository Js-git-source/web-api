package com.unisguard.webapi.service.apiclient.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiClientDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.ValidatorUtil;
import com.unisguard.webapi.manage.apiclient.ApiAuthManage;
import com.unisguard.webapi.manage.apiclient.ApiClientManage;
import com.unisguard.webapi.service.apiclient.ApiClientService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiClientServiceImpl implements ApiClientService {
    @Resource
    private ApiClientManage apiClientManage;
    @Resource
    private ApiAuthManage apiAuthManage;

    @Override
    public ResponseDO<List<ApiClientDO>> list(ApiClientDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<ApiClientDO> apiClientDOList = apiClientManage.list(param);
        return ResponseDO.success(apiClientDOList, page.getTotal());
    }

    @Override
    public ResponseDO<List<ApiDO>> apiList(ApiAuthDO param) {
        if (param.getClientId() == null || param.getClientId() == 0L) {
            throw new ApplicationException("客户端ID不能为空");
        }
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<ApiDO> authDOList = apiAuthManage.apiList(param);
        return ResponseDO.success(authDOList, page.getTotal());
    }

    @Override
    public ResponseDO<List<ApiDO>> notApiList(ApiAuthDO param) {
        if (param.getClientId() == null || param.getClientId() == 0L) {
            throw new ApplicationException("客户端ID不能为空");
        }
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<ApiDO> authDOList = apiAuthManage.notApiList(param);
        return ResponseDO.success(authDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(ApiClientDO param) {
        if (!ValidatorUtil.isUsername(param.getName())) {
            throw new ApplicationException("客户端名称只能包含中文和字母");
        }
        ApiClientDO apiClientDO = new ApiClientDO();
        apiClientDO.setName(param.getName());
        Integer exist = apiClientManage.exist(apiClientDO);
        if (exist > 0) {
            throw new ApplicationException("客户端名称已存在");
        }
        // 查询表里现有记录最大的appid，然后+1
        String maxAppId = apiClientManage.getMaxAppId();
        param.setAppid(maxAppId == null || "0".equals(maxAppId) ? "100000" : String.valueOf(Long.parseLong(maxAppId) + 1L));
        // 生成 appkey
        ResponseDO<String> appkey = createAppkey();
        param.setAppkey(appkey.getData());
        apiClientManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<ApiClientDO> detail(Long id) {
        ApiClientDO apiClientDO = apiClientManage.detail(id);
        return ResponseDO.success(apiClientDO);
    }

    @Override
    public ResponseDO<Long> edit(ApiClientDO param) {
        if (!ValidatorUtil.isUsername(param.getName())) {
            throw new ApplicationException("客户端名称只能包含中文和字母");
        }
        ApiClientDO detail = apiClientManage.detail(param.getId());
        if (!detail.getName().equals(param.getName())) {
            ApiClientDO apiClientDO = new ApiClientDO();
            apiClientDO.setName(param.getName());
            Integer exist = apiClientManage.exist(apiClientDO);
            if (exist > 0) {
                throw new ApplicationException("客户端名称已存在");
            }
        }
        apiClientManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        apiClientManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Boolean> editStatus(ApiClientDO param) {
        if (param.getId() == null) {
            throw new ApplicationException("数据编码不能为空");
        }
        if (param.getStatus() == 0) {
            throw new ApplicationException("状态不能为空");
        }
        apiClientManage.editStatus(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Boolean> apiAuth(List<ApiAuthDO> param) {
        for (ApiAuthDO apiAuthDO : param) {
            if (apiAuthDO.getClientId() == null || apiAuthDO.getClientId() == 0) {
                throw new ApplicationException("apiID不能为空");
            }
            if (apiAuthDO.getApiId() == null || apiAuthDO.getApiId() == 0) {
                throw new ApplicationException("客户端ID不能为空");
            }
            List<ApiAuthDO> list = apiAuthManage.list(apiAuthDO);
            if (list.size() > 0) {
                throw new ApplicationException("数据已存在");
            }
            apiAuthManage.add(apiAuthDO);
        }
        return ResponseDO.success(true);
    }

    @Override
    public ResponseDO<Boolean> cancelAuth(List<Long> param) {
        for (Long id : param) {
            apiAuthManage.delete(id);
        }
        return ResponseDO.success(true);
    }

    @Override
    public ResponseDO<String> createAppkey() {
        String appKey = RandomStringUtils.random(20, true, true).toLowerCase();
        return ResponseDO.success(appKey);
    }
}
package com.unisguard.webapi.service.apiclient;

import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiClientDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import java.util.List;
import java.util.Map;

public interface ApiClientService {
    ResponseDO<List<ApiClientDO>> list(ApiClientDO param);

    ResponseDO<List<ApiDO>> apiList(ApiAuthDO param);

    ResponseDO<List<ApiDO>> notApiList(ApiAuthDO param);

    ResponseDO<Long> add(ApiClientDO param);

    ResponseDO<ApiClientDO> detail(Long id);

    ResponseDO<Long> edit(ApiClientDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<Boolean> editStatus(ApiClientDO param);

    ResponseDO<Boolean> apiAuth(List<ApiAuthDO> param);

    ResponseDO<Boolean> cancelAuth(List<Long> param);

    ResponseDO<String> createAppkey();
}
package com.unisguard.webapi.service.apiclient;

import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import java.util.List;

public interface ApiAuthService {
    ResponseDO<List<ApiAuthDO>> list(ApiAuthDO param);

    ResponseDO<Long> add(ApiAuthDO param);

    ResponseDO<ApiAuthDO> detail(Long id);

    ResponseDO<Long> edit(ApiAuthDO param);

    ResponseDO<Long> delete(Long id);
}
package com.unisguard.webapi.manage.apiclient;

import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import java.util.List;

/**
 * @description api授权Mannge
 * @author sunsaike
 */
public interface ApiAuthManage {
    List<ApiAuthDO> list(ApiAuthDO param);

    List<ApiDO> apiList(ApiAuthDO param);

    List<ApiDO> notApiList(ApiAuthDO param);

    void add(ApiAuthDO param);

    ApiAuthDO detail(Long id);

    void edit(ApiAuthDO param);

    void delete(Long id);

    void cancelAuth(Long id);
}
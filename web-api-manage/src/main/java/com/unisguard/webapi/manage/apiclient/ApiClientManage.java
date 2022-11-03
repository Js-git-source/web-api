package com.unisguard.webapi.manage.apiclient;

import com.unisguard.webapi.common.dataobject.apiclient.ApiClientDO;

import java.util.List;

/**
 * @author sunsaike
 * @description api客户端Mannge
 */
public interface ApiClientManage {

    List<ApiClientDO> list(ApiClientDO param);

    void add(ApiClientDO param);

    ApiClientDO detail(Long id);

    void edit(ApiClientDO param);

    void delete(Long id);

    Boolean editStatus(ApiClientDO param);

    String getMaxAppId();

    Integer exist(ApiClientDO apiClientDO);
}
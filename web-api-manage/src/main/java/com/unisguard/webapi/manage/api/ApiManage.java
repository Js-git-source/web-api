package com.unisguard.webapi.manage.api;

import com.unisguard.webapi.common.dataobject.api.ApiDO;

import java.util.List;

/**
 * @author sunsaike
 * @description api管理Mansge
 */
public interface ApiManage {

    List<ApiDO> list(ApiDO param);

    void add(ApiDO param);

    ApiDO detail(Long id);

    void edit(ApiDO param);

    void delete(Long id);

    Integer exist(ApiDO apiDO);
}
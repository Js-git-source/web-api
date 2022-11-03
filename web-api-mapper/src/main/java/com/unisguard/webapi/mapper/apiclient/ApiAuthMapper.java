package com.unisguard.webapi.mapper.apiclient;

import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import java.util.List;

/**
 * @description api授权mapper类
 * @author sunsaike
 */
public interface ApiAuthMapper {
    /**
     * 列表
     * @param param api实体
     * @return List<ApiAuthDO>
     */
    List<ApiAuthDO> list(ApiAuthDO param);
    /**
     * api客户端已授权列表
     * @param param 页面表单赋值实体
     * @return List<ApiDO>
     */
    List<ApiDO> apiList(ApiAuthDO param);
    /**
     * api客户端未授权列表
     * @param param 页面表单赋值实体
     * @return List<ApiDO>
     */
    List<ApiDO> notApiList(ApiAuthDO param);
    /**
     * 新增
     * @param param 页面表单赋值实体
     * @return
     */
    void add(ApiAuthDO param);
    /**
     * 详情
     * @param id 主键id
     * @return ApiAuthDO api授权实体
     */
    ApiAuthDO detail(Long id);
    /**
     * 编辑
     * @param param 页面表单赋值实体
     * @return
     */
    void edit(ApiAuthDO param);
    /**
     * 删除
     * @param id 主键id
     * @return
     */
    void delete(Long id);
    /**
     * 取消授权
     * @param id api服务端实体的主键id
     * @return
     */
    void cancelAuth(Long id);
}
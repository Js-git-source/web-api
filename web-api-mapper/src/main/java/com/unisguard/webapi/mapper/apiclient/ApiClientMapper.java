package com.unisguard.webapi.mapper.apiclient;

import com.unisguard.webapi.common.dataobject.apiclient.ApiClientDO;
import java.util.List;
import java.util.Map;

/**
 * @description api客户端mapper类
 * @author sunsaike
 */
public interface ApiClientMapper {
    /**
     * 列表
     * @param param api客户端实体
     * @return List<ApiClientDO>
     */
    List<ApiClientDO> list(ApiClientDO param);
    /**
     * 添加
     * @param param api客户端实体
     * @return
     */
    void add(ApiClientDO param);
    /**
     * 详情
     * @param  id 主键id
     * @return 需要查询的api客户端实体
     */
    ApiClientDO detail(Long id);
    /**
     * 编辑
     * @param  param 页面最新的api客户端实体
     * @return
     */
    void edit(ApiClientDO param);
    /**
     * 删除
     * @param id 主键id
     * @return
     */
    void delete(Long id);
    /**
     * 修改状态
     * @param param 主api客户端实体
     * @return
     */
    Boolean editStatus(ApiClientDO param);
    /**
     * 获取最大的appid
     * @param
     * @return 最大的appid
     */
    String getMaxAppId();
    /**
     * 判断数据是否存在
     * @param param api客户端
     * @return 返回conunt的数量
     */
    Integer exist(ApiClientDO param);
}
package com.unisguard.webapi.mapper.api;

import com.unisguard.webapi.common.dataobject.api.ApiDO;

import java.util.List;

/**
 * @author sunsaike
 * @description api管理mapper类
 */
public interface ApiMapper {
    /**
     * 列表
     *
     * @param param api实体
     * @return List<ApiDO>
     */
    List<ApiDO> list(ApiDO param);

    /**
     * 查询某个api数据是否存在
     *
     * @param param api实体
     * @return Integer返回count的数量
     */
    Integer exist(ApiDO param);

    /**
     * 添加
     *
     * @param param api实体
     * @return
     */
    void add(ApiDO param);

    /**
     * 详情
     *
     * @param id 主键id
     * @return 需要查询的api实体
     */
    ApiDO detail(Long id);

    /**
     * 编辑
     *
     * @param param 页面最新的api实体
     * @return
     */
    void edit(ApiDO param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return
     */
    void delete(Long id);

}
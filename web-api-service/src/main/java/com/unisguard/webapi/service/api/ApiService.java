package com.unisguard.webapi.service.api;

import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import java.util.List;

/**
 * @description api管理service实现类
 * @author sunsaike
 */
public interface ApiService {
    /**
     * 列表
     * @param param 系统配置实体类
     * @return List<ApiDO>
     */
    ResponseDO<List<ApiDO>> list(ApiDO param);

    ResponseDO<Long> add(ApiDO param);

    ResponseDO<ApiDO> detail(Long id);

    ResponseDO<Long> edit(ApiDO param);

    ResponseDO<Long> delete(Long id);
}
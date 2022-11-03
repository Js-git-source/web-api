package com.unisguard.webapi.service.dict;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import java.util.List;
import java.util.Map;

public interface DictService {
    ResponseDO<List<DictDO>> list(DictDO param);

    ResponseDO<Long> add(DictDO param);

    ResponseDO<DictDO> detail(Long id);

    ResponseDO<Long> edit(DictDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<Boolean> batchDelete(List<Long> param);

    ResponseDO<List<Map<String,String>>> nodePath(DictDO param);

    ResponseDO<Boolean> refreshCache();

}
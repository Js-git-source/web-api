package com.unisguard.webapi.manage.dict;

import com.unisguard.webapi.common.dataobject.dict.DictDO;
import java.util.List;

public interface DictManage {
    List<DictDO> list(DictDO param);

    void add(DictDO param);

    DictDO detail(Long id);

    void edit(DictDO param);

    void delete(Long id);

    Integer exists(DictDO dictDO);

    List<DictDO> queryNodePath(DictDO param);
}
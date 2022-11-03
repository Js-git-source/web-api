package com.unisguard.webapi.mapper.mon;

import com.unisguard.webapi.common.dataobject.mon.MonDO;

import java.util.List;

public interface MonMapper {
    List<MonDO> list(MonDO param);

    void add(MonDO param);

    MonDO detail(MonDO param);

    void edit(MonDO param);

    void delete(Long id);

    List<MonDO> getEngineList(MonDO param);

    List<MonDO> indexList(MonDO param);

    List<MonDO> middlewareList(MonDO param);
}
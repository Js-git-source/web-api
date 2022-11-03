package com.unisguard.webapi.service.mon;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.mon.MonDO;
import com.unisguard.webapi.common.dataobject.mon.MonHisDO;

import java.util.List;
import java.util.Map;

public interface MonService {
    ResponseDO<List<MonDO>> list(MonDO param);

    ResponseDO<MonDO> serviceList(MonDO param);

    ResponseDO<List<MonDO>> getEngineList(MonDO param);

    ResponseDO<MonDO> cpu(MonDO param);

    ResponseDO<MonDO> ram(MonDO param);

    ResponseDO<MonDO> thread(MonDO param);

    ResponseDO<MonDO> connect(MonDO param);

    ResponseDO<MonDO> databaseConnect(MonDO param);

    ResponseDO<MonDO> throughput(MonDO param);

    ResponseDO<MonDO> network(MonDO param);

    ResponseDO<MonDO> io(MonDO param);

    ResponseDO<List<MonDO>> indexList(MonDO param);

    ResponseDO<MonDO> search(MonDO param);

    ResponseDO<MonDO> redisDetail(MonDO param);

    ResponseDO<List<MonDO>> middlewareList(MonDO param);

    ResponseDO<MonDO> usageCpu(MonDO param);

    ResponseDO<MonDO> usageRam(MonDO param);

    ResponseDO<MonDO> key(MonDO param);

    ResponseDO<List<MonDO>>  topic(MonDO param);

    ResponseDO<MonDO> flow(MonDO param);

    ResponseDO<MonDO> analysisUsageCpu(MonDO param);

    ResponseDO<MonDO> analysisUsageRam(MonDO param);

    ResponseDO<MonDO> analysisLoad(MonDO param);

}
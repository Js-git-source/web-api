package com.unisguard.webapi.manage.mon;

import com.unisguard.webapi.common.dataobject.mon.MonHisDO;
import java.util.List;

public interface MonHisManage {
    List<MonHisDO> list(MonHisDO param);

    MonHisDO detail(Long id);

    List<MonHisDO> cpu(MonHisDO param);

    List<MonHisDO> ram(MonHisDO param);

    List<MonHisDO> thread(MonHisDO param);

    List<MonHisDO> connect(MonHisDO param);

    List<MonHisDO> databaseConnect(MonHisDO param);

    List<MonHisDO> throughput(MonHisDO param);

    List<MonHisDO> io(MonHisDO param);

    List<MonHisDO> network(MonHisDO param);

    List<MonHisDO> search(MonHisDO param);

    List<MonHisDO> usageCpu(MonHisDO param);

    List<MonHisDO> key(MonHisDO param);

    List<MonHisDO> usageRam(MonHisDO param);

    List<MonHisDO> flow(MonHisDO param);

    List<MonHisDO> analysisUsageCpu(MonHisDO param);

    List<MonHisDO> analysisUsageRam(MonHisDO param);

    List<MonHisDO> analysisLoad(MonHisDO param);

    List<MonHisDO> analysisFlow(MonHisDO param);

    List<MonHisDO> middlewareDetailList(MonHisDO param);
}
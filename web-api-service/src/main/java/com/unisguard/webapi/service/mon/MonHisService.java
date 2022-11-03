package com.unisguard.webapi.service.mon;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.mon.MonDO;
import com.unisguard.webapi.common.dataobject.mon.MonHisDO;

import java.util.List;
import java.util.Map;

public interface MonHisService {

    ResponseDO<List<MonHisDO>> middlewareDetailList(MonHisDO param);

    ResponseDO<Map<String,List<MonHisDO>>> analysisFlow(MonHisDO param);
}
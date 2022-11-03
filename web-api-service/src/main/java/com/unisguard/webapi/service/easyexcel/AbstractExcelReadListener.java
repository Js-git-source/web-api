package com.unisguard.webapi.service.easyexcel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Head;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author jiangshan
 * @date 2022/1/11 17:21
 */
abstract class AbstractExcelReadListener<T> extends AnalysisEventListener<T> {
    void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext analysisContext, String msg) {
        headMap.values().removeIf(Objects::isNull);
        Map<Integer, Head> headPropertyMap = analysisContext.currentReadHolder().excelReadHeadProperty().getHeadMap();
        int size = headMap.size();
        if (size != headPropertyMap.size()) {
            throw new ExcelAnalysisException(msg);
        }
        for (int i = 0; i < size; i++) {
            Head head = headPropertyMap.get(i);
            if (head == null) {
                throw new ExcelAnalysisException(msg);
            }
            List<String> headNameList = head.getHeadNameList();
            if (CollectionUtils.isEmpty(headNameList)) {
                throw new ExcelAnalysisException(msg);
            }
            if (!headNameList.get(0).equals(headMap.get(i))) {
                throw new ExcelAnalysisException(msg);
            }
        }
    }
}

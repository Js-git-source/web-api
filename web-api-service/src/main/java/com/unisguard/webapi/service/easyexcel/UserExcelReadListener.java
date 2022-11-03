package com.unisguard.webapi.service.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.context.xlsx.DefaultXlsxReadContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserExcelDO;
import org.apache.commons.compress.utils.Lists;
import java.util.List;
import java.util.Map;

public class UserExcelReadListener extends AbstractExcelReadListener<UserExcelDO> {

    private List<UserDO> userDOList = Lists.newArrayList();

    private String account;


    public UserExcelReadListener(String account) {
        this.account = account;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext analysisContext) {
        super.invokeHeadMap(headMap, analysisContext, "用户导入模板错误");
    }

    @Override
    public void invoke(UserExcelDO userExcelDO, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

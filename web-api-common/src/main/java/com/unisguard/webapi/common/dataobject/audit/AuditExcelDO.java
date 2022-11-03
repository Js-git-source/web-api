package com.unisguard.webapi.common.dataobject.audit;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ExcelIgnoreUnannotated
public class AuditExcelDO {

    // 模块ID
    private Integer moduleId;
    // 操作类型
    private Integer opType;
    // 结果
    private Integer opRet;
    // 用户账号
    private String userAcc;

    @ExcelProperty(index = 0, value = "模块名称")
    @ColumnWidth(value = 18)
    private String moduleIdName;

    @ExcelProperty(index = 1, value = "操作用户")
    @ColumnWidth(value = 20)
    private String userName;

    @ExcelProperty(index = 2, value = "操作时间")
    @ColumnWidth(value = 20)
    private Date opTime;

    @ExcelProperty(index = 3, value = "操作类型")
    @ColumnWidth(value = 16)
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private String opTypeName;

    @ExcelProperty(index = 4, value = "操作结果")
    @ColumnWidth(value = 16)
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private String opRetName;

    @ExcelProperty(index = 5, value = "源IP地址")
    @ColumnWidth(value = 20)
    private String clientIp;

    @ExcelProperty(index = 6, value = "日志内容")
    @ColumnWidth(value = 80)
    private String msg;


}

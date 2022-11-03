package com.unisguard.webapi.manage.factory;


import com.unisguard.webapi.common.dataobject.audit.AuditDO;

import java.util.TimerTask;


public class WebLogAspect {


    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final AuditDO operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点

                System.out.println(operLog);

                System.out.println(operLog);

                //operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                //SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }


}

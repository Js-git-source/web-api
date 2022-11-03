package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author wangzemo
 * @date 2022/3/17 16:04
 */
public class SysConfigUtil {
    public static void initFirstLogin(List<SysConfigDO> sysConfigDOList) {
        if (CollectionUtils.isEmpty(sysConfigDOList)) {
            return;
        }
        sysConfigDOList.forEach(sysConfigDO -> {
            // 首次登录强制修改密码
            if (DictConstant.FIRST_FORCE.equals(sysConfigDO.getDataKey())) {
                GlobalConstant.FORCING_CHANGE_PASSWORD = DictConstant.TRUE.equals(Integer.parseInt(sysConfigDO.getDataValue()));
                // 登录最大失败次数
            } else if (DictConstant.MAX_FAIL_COUNT.equals(sysConfigDO.getDataKey())) {
                GlobalConstant.LOGIN_FAIL_COUNT = Integer.parseInt(sysConfigDO.getDataValue());
                // 登录失败锁定时间
            } else if (DictConstant.LOCK_TIME.equals(sysConfigDO.getDataKey())) {
                long LOGIN_LOCK_TIME = Long.parseLong(sysConfigDO.getDataValue());
                // 根据锁定时间单位转换成秒
                sysConfigDOList.stream()
                        .filter(item -> DictConstant.LOCK_TIME_UNIT.equals(item.getDataKey()))
                        .findFirst()
                        .ifPresent(item -> {
                            Integer dataValue = Integer.parseInt(item.getDataValue());
                            // 锁定时间单位为秒
                            if (DictConstant.LOCK_SECOND.equals(dataValue)) {
                                GlobalConstant.LOGIN_LOCK_TIME = LOGIN_LOCK_TIME * 1000;
                                GlobalConstant.LOGIN_LOCK_TIME_DESC = LOGIN_LOCK_TIME + "秒";
                                // 锁定时间单位为分钟
                            } else if (DictConstant.LOCK_MINUTE.equals(dataValue)) {
                                GlobalConstant.LOGIN_LOCK_TIME = LOGIN_LOCK_TIME * 60 * 1000;
                                GlobalConstant.LOGIN_LOCK_TIME_DESC = LOGIN_LOCK_TIME + "分钟";
                                // 锁定时间单位为小时
                            } else if (DictConstant.LOCK_HOUR.equals(dataValue)) {
                                GlobalConstant.LOGIN_LOCK_TIME = LOGIN_LOCK_TIME * 60 * 60 * 1000;
                                GlobalConstant.LOGIN_LOCK_TIME_DESC = LOGIN_LOCK_TIME + "小时";
                            }
                        });
            }
        });
    }
}

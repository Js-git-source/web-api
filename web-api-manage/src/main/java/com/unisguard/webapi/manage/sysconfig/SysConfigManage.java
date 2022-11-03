package com.unisguard.webapi.manage.sysconfig;

import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;

import java.util.List;

/**
 * @author sunsaike
 * @description 系统配置
 */
public interface SysConfigManage {
    List<SysConfigDO> list(SysConfigDO param);

    void add(SysConfigDO param);

    SysConfigDO detail(Long id);

    void edit(SysConfigDO param);

    void delete(Long id);

    List<SysConfigDO> queryAll();
}
package com.unisguard.webapi.mapper.sysconfig;

import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;

import java.util.List;

/**
 * @author sunsaike
 * @description 系统配置mapper类
 */
public interface SysConfigMapper {
    List<SysConfigDO> list(SysConfigDO param);

    void add(SysConfigDO param);

    SysConfigDO detail(Long id);

    void edit(SysConfigDO param);

    void delete(Long id);

    List<SysConfigDO> queryAll();
}
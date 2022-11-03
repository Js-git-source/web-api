package com.unisguard.webapi.mapper.lic;

import com.unisguard.webapi.common.dataobject.lic.LicDO;

import java.util.List;

public interface LicMapper {
    List<LicDO> list(LicDO param);

    void add(LicDO param);

    LicDO last();

    void edit(LicDO param);

    void delete(Long id);

    void updateStatus();
}
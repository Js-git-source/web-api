package com.unisguard.webapi.manage.lic;

import com.alibaba.fastjson.JSON;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.util.List;

public interface LicManage {
    List<LicDO> list(LicDO param);

    void add(LicDO param);

    LicDO last();

    void edit(LicDO param);

    void delete(Long id);
}
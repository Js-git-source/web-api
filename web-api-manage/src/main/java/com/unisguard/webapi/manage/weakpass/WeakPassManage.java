package com.unisguard.webapi.manage.weakpass;

import com.unisguard.webapi.common.dataobject.weakpass.WeakPassDO;
import java.util.List;

public interface WeakPassManage {
    List<WeakPassDO> list(WeakPassDO param);

    void add(WeakPassDO param);

    WeakPassDO detail(Long id);

    void edit(WeakPassDO param);

    void delete(Long id);
}
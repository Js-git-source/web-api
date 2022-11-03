package com.unisguard.webapi.service.dept;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;

import java.util.List;

public interface DeptService {
    ResponseDO<List<DeptDO>> list(DeptDO param);

    ResponseDO<Long> add(DeptDO param);

    ResponseDO<DeptDO> detail(Long id);

    ResponseDO<Long> edit(DeptDO param);

    ResponseDO<Long> delete(Long id);

}
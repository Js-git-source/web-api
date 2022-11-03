package com.unisguard.webapi.service.dept;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;
import java.util.List;

public interface DeptUserService {
    ResponseDO<List<DeptUserDO>> list(DeptUserDO param);

    ResponseDO<Long> add(DeptUserDO param);

    ResponseDO<DeptUserDO> detail(Long id);

    ResponseDO<Long> edit(DeptUserDO param);

    ResponseDO<Long> delete(Long id);

}
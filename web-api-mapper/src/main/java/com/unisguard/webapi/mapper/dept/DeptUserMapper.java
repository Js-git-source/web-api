package com.unisguard.webapi.mapper.dept;

import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;
import java.util.List;

public interface DeptUserMapper {
    List<DeptUserDO> list(DeptUserDO param);

    void add(DeptUserDO param);

    DeptUserDO detail(Long id);

    void edit(DeptUserDO param);

    void delete(Long id);

    List<DeptUserDO> selectDeptUserList(Long id);

    void deleteByDeptId(Long deptId);

    void insertBatch(List<DeptUserDO> deptUserDOList);


}
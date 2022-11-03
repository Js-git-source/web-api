package com.unisguard.webapi.manage.dept;

import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;

import java.util.List;

public interface DeptUserManage {
    List<DeptUserDO> list(DeptUserDO param);

    void add(DeptUserDO param);

    DeptUserDO detail(Long id);

    void edit(DeptUserDO param);

    void delete(Long id);

    List<DeptUserDO> selectDeptUserList(Long id);

    void deleteByDeptId(Long id);

    void insertBatch(List<DeptUserDO> DeptUserDOList);


}
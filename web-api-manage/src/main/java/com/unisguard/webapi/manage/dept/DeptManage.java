package com.unisguard.webapi.manage.dept;

import com.unisguard.webapi.common.dataobject.dept.DeptDO;

import java.util.List;

public interface DeptManage {
    List<DeptDO> list(DeptDO param);

    void add(DeptDO param);

    DeptDO detail(Long id);

    void edit(DeptDO param, String newPath, String oldPath, Long oldPid);

    void delete(Long id);

    Long checkRepeat(Long pid, String name);

    List<DeptDO> deptTree();

    List<DeptDO> deptUserTree();

    List<DeptDO> deptList();

    Integer checkExistChild(Long id);
}

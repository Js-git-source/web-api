package com.unisguard.webapi.mapper.dept;

import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper {
    List<DeptDO> list(DeptDO param);

    void add(DeptDO param);

    DeptDO detail(Long id);

    void edit(DeptDO param);

    void delete(Long id);

    Long checkRepeat(@Param("pid") Long pid, @Param("name") String name);

    List<DeptDO> deptTree();

    List<DeptDO> deptUserTree();

    List<DeptDO> deptList();

    Integer checkExistChild(Long id);
}

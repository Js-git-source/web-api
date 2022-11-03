package com.unisguard.webapi.manage.dept.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;
import com.unisguard.webapi.manage.dept.DeptUserManage;
import com.unisguard.webapi.mapper.dept.DeptUserMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class DeptUserManageImpl implements DeptUserManage {
    @Resource
    private DeptUserMapper deptUserMapper;

    public List<DeptUserDO> list(DeptUserDO param) {
        return deptUserMapper.list(param);
    }

    public void add(DeptUserDO param) {
        deptUserMapper.add(param);
    }

    public DeptUserDO detail(Long id) {
        return deptUserMapper.detail(id);
    }

    public void edit(DeptUserDO param) {
        deptUserMapper.edit(param);
    }

    public void delete(Long id) {
        deptUserMapper.delete(id);
    }

    @Override
    public List<DeptUserDO> selectDeptUserList(Long id) {
        return deptUserMapper.selectDeptUserList(id);
    }

    @Override
    public void deleteByDeptId(Long id) {
        deptUserMapper.deleteByDeptId(id);
    }

    @Override
    public void insertBatch(List<DeptUserDO> DeptUserDOList) {
        deptUserMapper.insertBatch(DeptUserDOList);
    }

}
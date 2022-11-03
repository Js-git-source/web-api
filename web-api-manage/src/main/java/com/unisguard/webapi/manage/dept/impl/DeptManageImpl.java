package com.unisguard.webapi.manage.dept.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.common.util.DeptUtil;
import com.unisguard.webapi.manage.dept.DeptManage;
import com.unisguard.webapi.mapper.dept.DeptMapper;
import com.unisguard.webapi.mapper.dept.DeptUserMapper;
import com.unisguard.webapi.mapper.user.UserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Manage
public class DeptManageImpl implements DeptManage {
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private DeptUserMapper deptUserMapper;
    @Resource
    private UserMapper userMapper;

    public List<DeptDO> list(DeptDO param) {
        return deptMapper.list(param);
    }

    @Override
    @Transactional
    public void add(DeptDO param) {
        deptMapper.add(param);
        // 保存部门关系人
        saveDeptUser(param);
    }

    private void saveDeptUser(DeptDO param) {
        List<DeptUserDO> deptUserDOList = new ArrayList<>();
        // 部门负责人
        if (CollectionUtils.isNotEmpty(param.getDeptHeadList())) {
            deptUserDOList.addAll(param.getDeptHeadList());
        }
        // 安全接口人
        if (CollectionUtils.isNotEmpty(param.getSafetyInterfaceList())) {
            deptUserDOList.addAll(param.getSafetyInterfaceList());
        }
        // 没有部门关系人
        if (CollectionUtils.isEmpty(deptUserDOList)) {
            return;
        }
        // 给用户添加部门关联ID
        deptUserDOList.forEach(item -> item.setDeptId(param.getId()));
        // 批量插入
        deptUserMapper.insertBatch(deptUserDOList);
    }

    @Override
    public DeptDO detail(Long id) {
        return deptMapper.detail(id);
    }

    @Override
    @Transactional
    public void edit(DeptDO param, String newPath, String oldPath, Long oldPid) {
        // 更新子部门路径
        updatePath(param, newPath, oldPath, oldPid);
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        deptMapper.edit(param);
        // 删除部门关系人
        deptUserMapper.deleteByDeptId(param.getId());
        // 保存部门关系人
        saveDeptUser(param);
    }

    /**
     * 更新子部门路径
     *
     * @param param
     * @param newPath
     * @param oldPath
     * @param oldPid
     */
    private void updatePath(DeptDO param, String newPath, String oldPath, Long oldPid) {
        if (StringUtils.isBlank(newPath)) {
            return;
        }
        param.setPath(newPath);
        // 查询所有部门
        List<DeptDO> deptDOList = deptMapper.list(new DeptDO());
        // 提取当前部门的子部门
        deptDOList = DeptUtil.treeTOList(deptDOList, oldPid);
        if (!deptDOList.isEmpty()) {
            deptDOList.forEach(deptDO -> {
                deptDO.setPath(deptDO.getPath().replace(oldPath, newPath));
                deptMapper.edit(deptDO);
            });
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 删除部门关系人
        deptUserMapper.deleteByDeptId(id);
        DeptDO deptDO = deptMapper.detail(id);
        Long deptId = deptDO == null || deptDO.getPid().equals(-1L) ? 0L : deptDO.getPid();
        // 更新用户所在部门
        userMapper.updateDeptId(id, deptId);
        deptMapper.delete(id);
    }

    @Override
    public Long checkRepeat(Long pid, String name) {
        return deptMapper.checkRepeat(pid, name);
    }

    @Override
    public List<DeptDO> deptTree() {
        return deptMapper.deptTree();
    }

    @Override
    public List<DeptDO> deptUserTree() {
        return deptMapper.deptUserTree();
    }

    @Override
    public List<DeptDO> deptList() {
        return deptMapper.deptList();
    }

    @Override
    public Integer checkExistChild(Long id) {
        return deptMapper.checkExistChild(id);
    }
}

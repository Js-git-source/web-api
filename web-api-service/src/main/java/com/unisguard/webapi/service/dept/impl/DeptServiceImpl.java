package com.unisguard.webapi.service.dept.impl;

import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.common.util.DeptUtil;
import com.unisguard.webapi.manage.dept.DeptManage;
import com.unisguard.webapi.manage.dept.DeptUserManage;
import com.unisguard.webapi.service.dept.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {
    @Resource
    private DeptManage deptManage;
    @Resource
    private DeptUserManage deptUserManage;

    @Override
    public ResponseDO<List<DeptDO>> list(DeptDO param) {
        List<DeptDO> deptDOList = deptManage.list(param);
        // 查询部门关系人
        List<DeptUserDO> deptUserDOList = deptUserManage.selectDeptUserList(null);
        for (DeptDO deptDO : deptDOList) {
            // 按用户类型分组
            Map<Integer, List<DeptUserDO>> deptUserDOMap = deptUserDOList.stream()
                    .filter(item -> item.getDeptId().equals(deptDO.getId()))
                    .collect(Collectors.groupingBy(DeptUserDO::getUserType));
            // 部门负责人
            deptDO.setDeptHeadList(deptUserDOMap.get(DictConstant.DEPT_HEAD));
            // 安全接口人
            deptDO.setSafetyInterfaceList(deptUserDOMap.get(DictConstant.SAFETY_INTERFACE));
        }
        // 名称和状态都为空
        if (StringUtils.isBlank(param.getName()) && (param.getStatus() == null || param.getStatus() == 0)) {
            deptDOList = DeptUtil.getTree(deptDOList, -1L);
            return ResponseDO.success(deptDOList);
        }
        deptDOList = deptDOList.stream()
                .filter(item -> -1 != item.getId())
                .filter(item -> StringUtils.isBlank(param.getName()) || item.getName().contains(param.getName()))
                .filter(item -> param.getStatus() == null || param.getStatus().equals(item.getStatus()))
                .collect(Collectors.toList());
        return ResponseDO.success(deptDOList);
    }

    @Override
    public ResponseDO<Long> add(DeptDO param) {
        // 同一个父部门下的子部门不能重复
        Long id = deptManage.checkRepeat(param.getPid(), param.getName());
        if (id != null) {
            return ResponseDO.failure("部门名称已存在");
        }
        // 组装path路径
        if (-1L == param.getPid()) {
            param.setPath("/" + param.getName());
        } else {
            DeptDO deptDO = deptManage.detail(param.getPid());
            if (deptDO == null) {
                return ResponseDO.failure("上级部门不存在");
            }
            param.setPath(deptDO.getPath() + "/" + param.getName());
        }
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        deptManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<DeptDO> detail(Long id) {
        DeptDO deptDO = deptManage.detail(id);
        // 查询部门管理人和安全接口人
        List<DeptUserDO> deptUserDOList = deptUserManage.selectDeptUserList(id);
        // 按用户类型分组
        Map<Integer, List<DeptUserDO>> deptUserDOMap = deptUserDOList.stream()
                .collect(Collectors.groupingBy(DeptUserDO::getUserType));
        // 部门负责人
        deptDO.setDeptHeadList(deptUserDOMap.get(DictConstant.DEPT_HEAD));
        // 安全接口人
        deptDO.setSafetyInterfaceList(deptUserDOMap.get(DictConstant.SAFETY_INTERFACE));
        return ResponseDO.success(deptDO);
    }

    @Override
    public ResponseDO<Long> edit(DeptDO param) {
        // 同一个父部门下的子部门不能重复
        Long id = deptManage.checkRepeat(param.getPid(), param.getName());
        if (id != null && !id.equals(param.getId())) {
            return ResponseDO.failure("部门名称已存在");
        }
        DeptDO deptDO = deptManage.detail(param.getId());
        // 获取部门路径
        String newPath = getNewPath(param, deptDO);
        deptManage.edit(param, newPath, deptDO.getPath(), deptDO.getPid());
        return ResponseDO.success();
    }

    private String getNewPath(DeptDO newDeptDO, DeptDO oldDeptDO) {
        // 上级部门不同
        if (!oldDeptDO.getPid().equals(newDeptDO.getPid())) {
            DeptDO parent = deptManage.detail(newDeptDO.getPid());
            if (parent == null) {
                throw new ApplicationException("上级部门不存在");
            }
            return parent.getPath() + "/" + newDeptDO.getName();
        }
        // 部门名称不同
        if (!oldDeptDO.getName().equals(newDeptDO.getName())) {
            int index = oldDeptDO.getPath().lastIndexOf("/");
            return oldDeptDO.getPath().substring(0, index) + "/" + newDeptDO.getName();
        }
        return "";
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        // 查询该部门下是否存在子部门
        Integer count = deptManage.checkExistChild(id);
        if (count != null) {
            return ResponseDO.failure("该部门下存在子部门不允许删除");
        }
        deptManage.delete(id);
        return ResponseDO.success();
    }
}
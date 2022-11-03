package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeptUtil {

    public static List<Long> getDeptId(List<DeptDO> deptDOList, Long parentId) {
        // 筛选父id的子部门
        List<DeptDO> list = deptDOList.stream().filter(deptTree -> parentId.equals(deptTree.getPid())).collect(Collectors.toList());
        // 提取子部门的id
        List<Long> deptIdList = list.stream().map(DeptDO::getId).collect(Collectors.toList());
        // 遍历子部门
        list.forEach(deptDO -> deptIdList.addAll(getDeptId(deptDOList, deptDO.getId())));
        return deptIdList;
    }

    public static List<DeptDO> getTree(List<DeptDO> deptDOList, Long parentId) {
        List<DeptDO> list = deptDOList.stream().filter(deptDO -> parentId.equals(deptDO.getPid())).collect(Collectors.toList());
        list.forEach(deptDO -> {
            List<DeptDO> childList = getTree(deptDOList, deptDO.getId());
            deptDO.setChildren(childList);
            Integer userCount = 0;
            for (DeptDO item : childList) {
                userCount += item.getCount();
            }
            deptDO.setCount(deptDO.getCount() + userCount);
        });
        return list;
    }

    public static List<DeptDO> treeTOList(List<DeptDO> deptDOList, Long parentId) {
        deptDOList = getTree(deptDOList, parentId);
        return treeTOList(deptDOList);
    }

    private static List<DeptDO> treeTOList(List<DeptDO> deptDOList) {
        List<DeptDO> list = new ArrayList<>();
        for (DeptDO deptDO : deptDOList) {
            list.add(deptDO);
            if (CollectionUtils.isNotEmpty(deptDO.getChildren())) {
                list.addAll(treeTOList(deptDO.getChildren()));
            }
        }
        return list;
    }
}

package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.dataobject.group.GroupDO;

import java.util.List;
import java.util.stream.Collectors;

public class GroupUtil {
    public static List<GroupDO> getTree(List<GroupDO> groupList, Long parentId) {
        List<GroupDO> list = groupList.stream().filter(groupDO -> parentId.equals(groupDO.getPid())).collect(Collectors.toList());
        list.forEach(groupDO -> {
            List<GroupDO> childList = getTree(groupList, groupDO.getId());
            groupDO.setChildren(childList);
        });
        return list;
    }
}

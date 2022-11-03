package com.unisguard.webapi.service.dept.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptUserDO;
import com.unisguard.webapi.manage.dept.DeptUserManage;
import com.unisguard.webapi.service.dept.DeptUserService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DeptUserServiceImpl implements DeptUserService {
    @Resource
    private DeptUserManage deptUserManage;

    public ResponseDO<List<DeptUserDO>> list(DeptUserDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<DeptUserDO> deptUserDOList = deptUserManage.list(param);
        return ResponseDO.success(deptUserDOList, page.getTotal());
    }

    public ResponseDO<Long> add(DeptUserDO param) {
        deptUserManage.add(param);
        return ResponseDO.success(param.getId());
    }

    public ResponseDO<DeptUserDO> detail(Long id) {
        DeptUserDO deptUserDO = deptUserManage.detail(id);
        return ResponseDO.success(deptUserDO);
    }

    public ResponseDO<Long> edit(DeptUserDO param) {
        deptUserManage.edit(param);
        return ResponseDO.success();
    }

    public ResponseDO<Long> delete(Long id) {
        deptUserManage.delete(id);
        return ResponseDO.success();
    }

}
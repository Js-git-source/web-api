package com.unisguard.webapi.service.dict.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.ValidatorUtil;
import com.unisguard.webapi.manage.dict.DictManage;
import com.unisguard.webapi.service.dict.DictService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl implements DictService {
    @Resource
    private DictManage dictManage;

    @Override
    public ResponseDO<List<DictDO>> list(DictDO param) {
        Page<Object> page = PageHelper.startPage(param.getPage(), param.getLimit());
        // 分页查询数据字典
        List<DictDO> dictDOList = dictManage.list(param);
        return ResponseDO.success(dictDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(DictDO param) {
        // 参数校验
        check(param);
        // 数据字典编码判重
        DictDO dictDO = new DictDO();
        dictDO.setCode(param.getCode());
        Integer count = dictManage.exists(dictDO);
        if (count > 0) {
            throw new ApplicationException("字典编号已存在");
        }
        // 添加
        dictManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<DictDO> detail(Long id) {
        // 根据id查询字典详情
        DictDO dictDO = dictManage.detail(id);
        return ResponseDO.success(dictDO);
    }

    @Override
    public ResponseDO<Long> edit(DictDO param) {
        // 参数校验
        check(param);
        // 数据字典判重
        DictDO dictDOOld = dictManage.detail(param.getId());
        Integer code = dictDOOld.getCode();
        if (code.intValue() != param.getCode().intValue()) {
            DictDO dictDO = new DictDO();
            dictDO.setCode(param.getCode());
            Integer count = dictManage.exists(dictDO);
            if (count > 0) {
                throw new ApplicationException("字典编号已存在");
            }
        }
        // 修改
        dictManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        // 删除问题
        DictDO param = dictManage.detail(id);
        if (param == null) {
            throw new ApplicationException("数据字典不存在");
        }
        DictDO dictDO = new DictDO();
        dictDO.setCodeType(param.getCode());
        List<DictDO> dictDOList = dictManage.list(dictDO);
        if (dictDOList.size() > 0) {
            throw new ApplicationException("数据字典[" + param.getName() + "]存在下级字典，不能删除");
        }
        dictManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Boolean> batchDelete(List<Long> ids) {
        List<String> list = new ArrayList<>();
        for (Long id : ids) {
            DictDO param = dictManage.detail(id);
            DictDO dictDO = new DictDO();
            dictDO.setCodeType(param.getCode());
            List<DictDO> dictDOList = dictManage.list(dictDO);
            if (dictDOList.size() > 0) {
                list.add(param.getName());
            }
        }
        if (list.size() > 0) {
            String msg = String.join(",", list);
            throw new ApplicationException("数据字典[" + msg + "]存在下级字典，不能删除");
        } else {
            ids.forEach(id -> dictManage.delete(id));
        }

        return ResponseDO.success();
    }

    @Override
    public ResponseDO<List<Map<String, String>>> nodePath(DictDO param) {
        List<Map<String, String>> list = new ArrayList<>();
        // 根据条件查询数据字典
        List<DictDO> dictDOList = dictManage.queryNodePath(param);
        // 过滤集合中父节点不相同的集合
        Map<Integer, Long> collect = dictDOList.stream().collect(Collectors.groupingBy(DictDO::getCodeType,
                Collectors.counting()));
        // 过滤集合中梳理大于1，有不同的父节点数据
        if (collect.size() > 1 || dictDOList.size() == 0) {
            // 默认根节点
            Map<String, String> result = new HashMap<>();
            result.put("codeType", "-1");
            result.put("codeTypeName", "根目录");
            list.add(result);
            return ResponseDO.success(list);
        }
        // 过滤集合中梳理小于等于1，有相同父节点数据
        DictDO dictDO = dictDOList.get(0);
        Integer codeType = dictDO.getCodeType();
        getDictParId(list, codeType);
        return ResponseDO.success(list);
    }

    @Override
    public ResponseDO<Boolean> refreshCache() {
        List<DictDO> dictDOList = dictManage.list(new DictDO());
        if (CollectionUtils.isEmpty(dictDOList)) {
            return ResponseDO.success(false);
        }
        Map<Integer, List<DictDO>> dictDOMap = dictDOList.stream().collect(Collectors.groupingBy(DictDO::getCodeType));
        GlobalConstant.DICT_MAP.clear();
        GlobalConstant.DICT_MAP.putAll(dictDOMap);
        return ResponseDO.success(true);
    }

    private void getDictParId(List<Map<String,String>> list,Integer codeType){
        DictDO param=new DictDO();
        param.setCode(codeType);
        List<DictDO> dictDOList = dictManage.queryNodePath(param);
        Map<String, String> map;
        if (dictDOList.size() > 0) {
            map = new HashMap<>();
            DictDO dictDO = dictDOList.get(0);
            Integer dicCodeType = dictDO.getCodeType();
            Integer code = dictDO.getCode();
            String name = dictDO.getName();
            map.put("codeType", String.valueOf(code));
            map.put("codeTypeName", name == null ? "根目录" : name);
            getDictParId(list, dicCodeType);
        } else {
            map = new HashMap<>();
            map.put("codeType", "-1");
            map.put("codeTypeName", "根目录");
        }
        list.add(map);

    }

    private void check(DictDO param) {
        if (!ValidatorUtil.isUsername(param.getName())) {
            throw new ApplicationException("字典名称只能包含中文和字母");
        }
        if (!Pattern.matches("^\\d{1,8}$", String.valueOf(param.getCode()))) {
            throw new ApplicationException("字典编码只能输入1-8位整数");
        }
        if (!Pattern.matches("^(-1)$", String.valueOf(param.getCodeType())) && !Pattern.matches("^\\d{1," +
                        "8}$",
                String.valueOf(param.getCodeType()))) {
            throw new ApplicationException("分类编号只能输入1-8位整数");
        }
        if (!Pattern.matches("^\\d{1,4}$", String.valueOf(param.getLevel()))) {
            throw new ApplicationException("字典顺序只能输入1-4位整数");
        }
    }
}
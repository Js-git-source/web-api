package com.unisguard.webapi.service.init;

import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.manage.dict.DictManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zemel
 * @date 2022/1/4 17:13
 */
@Service
public class DictInitService implements InitializingBean {
    @Resource
    private DictManage dictManage;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<DictDO> dictDOList = dictManage.list(new DictDO());
        if (CollectionUtils.isEmpty(dictDOList)) {
            return;
        }
        Map<Integer, List<DictDO>> dictDOMap = dictDOList.stream().collect(Collectors.groupingBy(DictDO::getCodeType));
        GlobalConstant.DICT_MAP.clear();
        GlobalConstant.DICT_MAP.putAll(dictDOMap);
    }
}

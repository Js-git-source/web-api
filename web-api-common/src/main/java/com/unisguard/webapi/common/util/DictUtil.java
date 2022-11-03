package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author zemel
 * @date 2022/1/4 17:35
 */
public class DictUtil {
    /**
     * 根据字典码查询数据字典
     *
     * @param codeType 分类编号
     * @param code     数据编号
     * @return DictDO
     */
    public static DictDO getByCode(Integer codeType, Integer code) {
        if (codeType == null || code == null) {
            return null;
        }
        List<DictDO> dictDOList = GlobalConstant.DICT_MAP.get(codeType);
        if (CollectionUtils.isEmpty(dictDOList)) {
            return null;
        }
        return dictDOList.stream().filter(dictDO -> dictDO.getCode().equals(code)).findFirst().orElse(null);
    }

    /**
     * 根据字典名称查询数据字典
     *
     * @param codeType 分类编号
     * @param name     字典编号
     * @return DictDO
     */
    public static DictDO getByName(Integer codeType, String name) {
        if (codeType == null || StringUtils.isBlank(name)) {
            return null;
        }
        List<DictDO> dictDOList = GlobalConstant.DICT_MAP.get(codeType);
        if (CollectionUtils.isEmpty(dictDOList)) {
            return null;
        }
        return dictDOList.stream().filter(dictDO -> dictDO.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * 根据分类编号查询数据字典
     *
     * @param codeType 分类编号
     * @return List<DictDO>
     */
    public static List<DictDO> getByCodeType(Integer codeType) {
        if (codeType == null) {
            return null;
        }
        return GlobalConstant.DICT_MAP.get(codeType);
    }

    /**
     * 根据code获取名称
     *
     * @param code
     * @return
     */
    public static String getNameByCode(Integer code) {
        if (code == null) {
            return "";
        }
        Optional<DictDO> optional = GlobalConstant.DICT_MAP.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getCode().equals(code))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get().getName();
        }
        return "";
    }
}

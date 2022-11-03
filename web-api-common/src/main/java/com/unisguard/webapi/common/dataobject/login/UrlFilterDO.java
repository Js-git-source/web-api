package com.unisguard.webapi.common.dataobject.login;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangzemo
 * @date 2022/1/18 15:06
 */
@Setter
@Getter
public class UrlFilterDO {
    // 描述
    private String description;

    // 标识
    private String key;

    // url集合
    private List<String> urlList;
}

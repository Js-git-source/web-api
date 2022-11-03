package com.unisguard.webapi.service.menu;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuUrlDO;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;


public interface MenuUrlService {
    ResponseDO<List<MenuUrlDO>> list(MenuUrlDO param);

    ResponseDO<Long> add(List<MenuUrlDO> param);

    ResponseDO<MenuUrlDO> detail(Long id);

    ResponseDO<Long> edit(MenuUrlDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<List<MenuUrlDO>> query(MenuUrlDO param, RequestMappingHandlerMapping requestMappingHandlerMapping, String realPath);

}
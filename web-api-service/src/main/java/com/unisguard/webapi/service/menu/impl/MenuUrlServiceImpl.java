package com.unisguard.webapi.service.menu.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuUrlDO;
import com.unisguard.webapi.manage.menu.MenuUrlManage;
import com.unisguard.webapi.service.menu.MenuUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuUrlServiceImpl implements MenuUrlService {
    @Resource
    private MenuUrlManage menuUrlManage;

    @Override
    public ResponseDO<List<MenuUrlDO>> list(MenuUrlDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<MenuUrlDO> menuUrlDOList = menuUrlManage.list(param);
        return ResponseDO.success(menuUrlDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(List<MenuUrlDO> param) {
        menuUrlManage.add(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<MenuUrlDO> detail(Long id) {
        MenuUrlDO menuUrlDO = menuUrlManage.detail(id);
        return ResponseDO.success(menuUrlDO);
    }

    @Override
    public ResponseDO<Long> edit(MenuUrlDO param) {
        menuUrlManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        menuUrlManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<List<MenuUrlDO>> query(MenuUrlDO param, RequestMappingHandlerMapping requestMappingHandlerMapping, String realPath) {
        // 查询已经添加的授权
        List<MenuUrlDO> haveList = menuUrlManage.queryListByMenuId(param.getMenuId());
        List<String> urlList = haveList.stream().map(MenuUrlDO::getUrl).collect(Collectors.toList());
        // 获取控制层的接口
        List<MenuUrlDO> menuAuthDOList = getRequestPath(requestMappingHandlerMapping);
        menuAuthDOList = menuAuthDOList.stream()
                .filter(menuAuthDO -> !urlList.contains(menuAuthDO.getUrl()))
                .filter(menuAuthDO -> param.getUrl() == null || menuAuthDO.getUrl().contains(param.getUrl()))
                .collect(Collectors.toList());
        int fromIndex = (param.getPage() - 1) * param.getLimit();
        int toIndex = Math.min(param.getPage() * param.getLimit(), menuAuthDOList.size());
        return ResponseDO.success(menuAuthDOList.subList(fromIndex, toIndex), (long) menuAuthDOList.size());
    }

    private List<MenuUrlDO> getRequestPath(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(requestMapping -> {
                    RequestMappingInfo requestMappingInfo = requestMapping.getKey();
                    // 权限地址
                    String url = StringUtils.collectionToDelimitedString(requestMappingInfo.getPatternsCondition().getPatterns(), ",");
                    MenuUrlDO menuUrlDO = new MenuUrlDO();
                    menuUrlDO.setUrl(url);
                    HandlerMethod handlerMethod = requestMapping.getValue();
                    // 控制器类
                    Class<?> controllerType = handlerMethod.getBeanType();
                    if (controllerType.isAnnotationPresent(Api.class) && controllerType.getAnnotation(Api.class).tags().length > 0) {
                        menuUrlDO.setName(controllerType.getAnnotation(Api.class).tags()[0]);
                    }
                    // 方法
                    Method method = handlerMethod.getMethod();
                    if (method.isAnnotationPresent(ApiOperation.class)) {
                        menuUrlDO.setName(menuUrlDO.getName() + "-" + method.getAnnotation(ApiOperation.class).value());
                    }
                    return menuUrlDO;
                }).collect(Collectors.toList());
    }
}
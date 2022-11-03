package com.unisguard.webapi.service.init;

import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.manage.menu.MenuManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangzemo
 * @date 2022/2/10 17:40
 */
@Service
public class MenuInitService implements InitializingBean {
    @Resource
    private MenuManage menuManage;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<MenuDO> menuDOList = menuManage.list();
        if (CollectionUtils.isEmpty(menuDOList)) {
            return;
        }
        GlobalConstant.MENU_LIST.addAll(menuDOList.stream().filter(menuDO -> 3 == menuDO.getLevel()).map(MenuDO::getUrl).collect(Collectors.toList()));
    }
}

package com.unisguard.webapi.service.login;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.LoginDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author zemel
 * @date 2022/1/9 20:16
 */
public interface LoginService {
    CurrentUserDO login(LoginDO param, String authCode);

    String authCode(OutputStream out) throws IOException;

    ResponseDO<List<MenuDO>> menu(CurrentUserDO currentUserDO);
}

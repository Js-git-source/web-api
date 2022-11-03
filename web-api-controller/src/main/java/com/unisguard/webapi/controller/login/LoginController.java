package com.unisguard.webapi.controller.login;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.LoginDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.service.login.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author zemel
 * @date 2022/1/9 19:34
 */
@Api(tags = "登录管理")
@RestController
@RequestMapping(value = "/login")
public class LoginController {
    @Resource
    private LoginService loginService;

    @ApiOperation(value = "登录")
    @ApiOperationSupport(order = 10)
    @PostMapping("/index")
    public ResponseDO login(@RequestBody LoginDO param, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String authCode = (String) session.getAttribute(GlobalConstant.AUTH_CODE);
        session.removeAttribute(GlobalConstant.AUTH_CODE);
        CurrentUserDO currentUserDO = loginService.login(param, authCode);
        session.setAttribute(GlobalConstant.CURRENT_USER, currentUserDO);
        GlobalConstant.SESSION_LIST.add(session);
        return ResponseDO.success(currentUserDO);
    }

    @ApiOperation(value = "验证码", produces = "image/jpeg")
    @ApiOperationSupport(order = 20)
    @GetMapping("/auth/code")
    public void createRandomImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String authCode = loginService.authCode(response.getOutputStream());
        session.setAttribute(GlobalConstant.AUTH_CODE, authCode);
    }

    @ApiOperation(value = "菜单")
    @ApiOperationSupport(order = 30)
    @GetMapping("/menu")
    public ResponseDO<List<MenuDO>> menu() {
        return loginService.menu(CurrentUserUtil.get());
    }

    @ApiOperation(value = "退出")
    @ApiOperationSupport(order = 40)
    @GetMapping("/logout")
    public ResponseDO logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        GlobalConstant.SESSION_LIST.remove(session);
        session.removeAttribute(GlobalConstant.CURRENT_USER);
        session.removeAttribute(GlobalConstant.AUTH_CODE);
        session.invalidate();
        return ResponseDO.success();
    }
}

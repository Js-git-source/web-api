package com.unisguard.webapi.config.listener;

import com.unisguard.webapi.common.constant.GlobalConstant;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author wangzemo
 * @date 2022/2/21 14:17
 */
public class SessionListener implements HttpSessionListener {
    /**
     * 只要一打开浏览器就会执行 ,没有登陆也会执行.
     *
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    /**
     * 只有超时,invalidate()才会执行
     * session.invalidate() ,session才会destroy
     *
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        GlobalConstant.SESSION_LIST.remove(se.getSession());
    }
}

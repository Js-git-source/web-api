package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author wangzemo
 * @date 2022/1/18 11:15
 */
public class CurrentUserUtil {
    public static CurrentUserDO get() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpSession session = request.getSession();
        return (CurrentUserDO) session.getAttribute(GlobalConstant.CURRENT_USER);
    }

    public static Long getId() {
        CurrentUserDO currentUserDO = get();
        if (currentUserDO == null) {
            return null;
        }
        return currentUserDO.getId();
    }
}

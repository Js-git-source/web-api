package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Iterator;

/**
 * @author wangzemo
 * @date 2022/2/15 10:04
 */
public class SessionUtil {
    public static void remove(String account) {
        if (StringUtils.isBlank(account)) {
            return;
        }
        Iterator<HttpSession> iterator = GlobalConstant.SESSION_LIST.iterator();
        while (iterator.hasNext()) {
            HttpSession session = iterator.next();
            if (session == null) {
                iterator.remove();
                iterator = GlobalConstant.SESSION_LIST.iterator();
                continue;
            }
            CurrentUserDO currentUserDO = (CurrentUserDO) session.getAttribute(GlobalConstant.CURRENT_USER);
            if (currentUserDO == null || account.equals(currentUserDO.getAccount())) {
                iterator.remove();
                iterator = GlobalConstant.SESSION_LIST.iterator();
                session.removeAttribute(GlobalConstant.CURRENT_USER);
                session.removeAttribute(GlobalConstant.AUTH_CODE);
            }
        }
    }
}

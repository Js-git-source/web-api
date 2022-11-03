package com.unisguard.webapi.common.util;

import java.net.URL;

/**
 * @author wangzemo
 * @date 2022/3/1 16:43
 */
public class ResourceUtil {

    public static String getRootPath() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        if (resource == null) {
            return "";
        }
        return resource.getPath();
    }
}

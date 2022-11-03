package com.unisguard.webapi.common.util;

/**
 * @Description: 执行shell命令工具类
 * @date 2022-1-24
 */
public class ShellUtil {

    public static boolean execShell(String cmd) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        int code = p.waitFor();
        p.destroy();
        return code == 0;
    }
}

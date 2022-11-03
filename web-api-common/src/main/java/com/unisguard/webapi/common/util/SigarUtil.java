package com.unisguard.webapi.common.util;


import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;

import java.io.File;
import java.io.IOException;

public class SigarUtil {

    public final static Sigar sigar = initSigar();
    public final static OperatingSystem os = initOs();

    private static Sigar initSigar() {
        try {
            init();
            return new Sigar();
        } catch (Exception e) {
            return null;
        }
    }

    private static void init() throws IOException {
        String rootPath = ResourceUtil.getRootPath();
        File classPath = new File(rootPath + "/sigar/.sigar_shellrc").getParentFile();
        String path = System.getProperty("java.library.path");
        if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
            path += ":" + classPath.getCanonicalPath();
        } else {
            path += ";" + classPath.getCanonicalPath();
        }
        System.setProperty("java.library.path", path);
    }

    private static OperatingSystem initOs() {
        try {
            init();
            return OperatingSystem.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

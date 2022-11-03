package com.unisguard.webapi.common.util;

import com.alibaba.fastjson.JSON;
import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.common.dataobject.lic.RequestAuthDO;
import com.unisguard.webapi.common.dataobject.lic.ResponseAuthDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;

public class LicUtil {
    private static final Logger log = LoggerFactory.getLogger(LogConstant.SYS);

    private static String publicKey = "";

    /**
     * 创建申请证书的文件内容
     *
     * @return String
     */
    public static String createRequestKey() throws Exception {
        RequestAuthDO req = new RequestAuthDO();
        Properties props = System.getProperties();
        // 操作系统的名称
        req.setOsName(props.getProperty("os.name"));
        // 操作系统的架构
        req.setOsArch(props.getProperty("os.arch"));
        // 用户的账户名称
        req.setUserName(props.getProperty("user.name"));
        // 用户的主目录
        req.setUserHome(props.getProperty("user.home"));
        // 用户的当前工作目录
        req.setUserDir(props.getProperty("user.dir"));
        // 创建时间
        req.setCreateTime(LocalDateTime.now());
        // MAC地址
        req.setMacAddr(getLocalMac());
        // 加密
        return encrypt(JSON.toJSONString(req));
    }

    /**
     * 加密
     *
     * @param content 内容
     * @return String
     */
    private static String encrypt(String content) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        int MAX_ENCRYPT_BLOCK = 117;
        int offSet = 0;
        byte[] cache;
        int inputLength = content.length();
        byte[] inputArray = content.getBytes();
        String text = null;
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            if (StringUtils.isBlank(text)) {
                text = Base64.encodeBase64String(cache);
            } else {
                text += "$NgSoC$" + Base64.encodeBase64String(cache);
            }
        }
        return text;
    }

    /**
     * 解密
     *
     * @param content 内容
     * @return String
     */
    private static String decrypt(String content) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        String[] arrays = content.split("\\$NgSoC\\$");
        List<byte[]> byteList = new ArrayList<>();
        int count = 0;
        for (String sub : arrays) {
            byte[] result = cipher.doFinal(Base64.decodeBase64(sub));
            byteList.add(result);
            count += result.length;
        }
        byte[] ret = new byte[count];
        count = 0;
        for (byte[] bytes : byteList) {
            System.arraycopy(bytes, 0, ret, count, bytes.length);
            count += bytes.length;
        }
        return new String(ret, StandardCharsets.UTF_8);
    }

    /**
     * 获取Cipher
     *
     * @param opMode 操作方式
     * @return Cipher
     */
    private static Cipher getCipher(int opMode) throws Exception {
        // 获取公钥
        String publicKeyStr = getPublicKey();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
        KeyFactory keyFactory;
        PublicKey publicKey;
        keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(opMode, publicKey);
        return cipher;
    }

    /**
     * 获取公钥
     *
     * @return String
     */
    private static String getPublicKey() throws Exception {
        if (StringUtils.isBlank(publicKey)) {
            String rootPath = ResourceUtil.getRootPath();
            if (StringUtils.isBlank(rootPath)) {
                return "";
            }
            String path = rootPath + "/config/lic.key";
            InputStream input = new FileInputStream(path);
            publicKey = IOUtils.toString(input, Charset.defaultCharset());
            input.close();
        }
        return publicKey;
    }

    /**
     * 获取本机MAC地址
     *
     * @return String
     * @throws SigarException
     */
    private static String getLocalMac() throws Exception {
        Sigar sigar = SigarUtil.sigar;
        if (sigar == null) {
            return null;
        }
        StringBuilder mac = new StringBuilder();
        List<String> ipList = getHostAddress(sigar);
        String[] ifNames = sigar.getNetInterfaceList();
        for (String ip : ipList) {
            for (String name : ifNames) {
                NetInterfaceConfig config = sigar.getNetInterfaceConfig(name);
                if (ip.equals(config.getAddress())) {
                    mac.append(",").append(config.getHwaddr());
                }
            }
        }
        return mac.substring(1);
    }

    private static List<String> getHostAddress(Sigar sigar) throws Exception {
        List<String> list = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> addresses = networkInterfaces.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    // ipv4且站点本地地址
                    if (inetAddress instanceof Inet4Address && inetAddress.isSiteLocalAddress()) {
                        list.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            list.clear();
            list.add(sigar.getFQDN());
        }
        list.sort(Comparator.comparing(item -> {
            try {
                return toNumber(item);
            } catch (UnknownHostException e) {
                return 0L;
            }
        }));
        if (list.isEmpty()) {
            list.add("127.0.0.1");
        }
        return list;
    }

    /**
     * IP转换为数值
     *
     * @param ipAddress ip地址
     * @return 数值
     * @throws UnknownHostException 异常
     */
    private static long toNumber(String ipAddress) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName(ipAddress);
        byte[] b = ip.getAddress();
        return b[0] << 24L & 0xff000000L |
                b[1] << 16L & 0xff0000L |
                b[2] << 8L & 0xff00L |
                b[3] << 0L & 0xffL;
    }

    /**
     * 检查授权
     *
     * @param lic
     * @return
     * @throws Exception
     */
    public static ResponseAuthDO checkLic(String lic) throws Exception {
        // 解密
        String jsonStr = decrypt(lic);
        // 转成对象
        ResponseAuthDO responseAuthDO = JSON.parseObject(jsonStr, ResponseAuthDO.class);
        // 系统属性
        Properties props = System.getProperties();
        // 操作系统的名称
        if (!props.getProperty("os.name").equals(responseAuthDO.getOsName())) {
            log.warn("操作系统的名称错误");
            throw new ApplicationException("授权参数错误");
        }
        // 操作系统的架构
        if (!props.getProperty("os.arch").equals(responseAuthDO.getOsArch())) {
            log.warn("操作系统的架构错误");
            throw new ApplicationException("授权参数错误");
        }
        // 用户的账户名称
        if (!props.getProperty("user.name").equals(responseAuthDO.getUserName())) {
            log.warn("用户的账户名称错误");
            throw new ApplicationException("授权参数错误");
        }
        // 用户的主目录
        if (!props.getProperty("user.home").equals(responseAuthDO.getUserHome())) {
            log.warn("用户的主目录错误");
            throw new ApplicationException("授权参数错误");
        }
        // 用户的当前工作目录
        if (!props.getProperty("user.dir").equals(responseAuthDO.getUserDir())) {
            log.warn("用户的当前工作目录错误");
            throw new ApplicationException("授权参数错误");
        }
        // MAC地址
        String localMac = getLocalMac();
        if (localMac == null || !localMac.equals(responseAuthDO.getMacAddr())) {
            log.warn("MAC地址错误");
            throw new ApplicationException("授权参数错误");
        }
        // 授权周期为-1，代表无限制
        if (responseAuthDO.getAuthCycle() == -1) {
            return responseAuthDO;
        }
        // 授权时间+授权周期 小于 当前时间
        if (responseAuthDO.getAuthTime().plusDays(responseAuthDO.getAuthCycle()).isAfter(LocalDateTime.now())) {
            log.warn("授权已过期");
            throw new ApplicationException("授权已过期");
        }
        return responseAuthDO;
    }
}

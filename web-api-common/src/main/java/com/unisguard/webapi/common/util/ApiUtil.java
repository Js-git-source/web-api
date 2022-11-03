package com.unisguard.webapi.common.util;

public class ApiUtil {

    /**
     * 用于校检客户端传递BA认证串的正确性
     *
     * @param clientId     客户端id,存储在服务端数据库
     * @param clientSecret 客户端密钥,存储在服务端数据库,与客户端id对应关系
     * @param date         时间戳
     * @return 加密串
     */
    public static String createClientSecret(String clientId, String clientSecret, Long date) {
        String stringToSign = clientId + clientSecret + date;
        return Sha256Hash.getSHA256Hash(stringToSign, "%NgSoc@!tye");
    }
}

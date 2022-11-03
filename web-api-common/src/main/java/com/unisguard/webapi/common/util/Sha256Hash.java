package com.unisguard.webapi.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@Slf4j
public class Sha256Hash {

    private Sha256Hash() {

    }

    public static String getSHA256Hash(String data, String salt) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            data = salt + data;
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hash);
        } catch (Exception e) {
            log.error("error message = " + e.getMessage(), e);
        }
        return result;
    }

    public static String getSHA1Hash(String data, String salt) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            data = salt + data;
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hash);
        } catch (Exception e) {
            log.error("error message = " + e.getMessage(), e);
        }
        return result;
    }

}

package com.unisguard.webapi.web.test;

import org.jasypt.util.text.AES256TextEncryptor;
import org.junit.jupiter.api.Test;

/**
 * @author zemel
 * @date 2021/12/30 21:35
 */
class JasyptTest {

    @Test
    void test256() {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword("n*g^s%o$c#");
        System.out.println(encryptor.encrypt("ngSOC#1234"));
    }
}

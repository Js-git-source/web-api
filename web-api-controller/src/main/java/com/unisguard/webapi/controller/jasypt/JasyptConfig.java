package com.unisguard.webapi.controller.jasypt;

import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author zemel
 * @date 2021/12/31 14:26
 */

@Configuration(value = "jasyptAutoConfig")
@EnableConfigurationProperties(value = {JasyptEncryptorConfigurationProperties.class})
public class JasyptConfig {
    @Resource
    private JasyptEncryptorConfigurationProperties properties;

    @Bean(name = "jasyptStringEncryptor")
    @ConditionalOnMissingBean(name = {"jasyptStringEncryptor"})
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("n*g^s%o$c#");
        config.setAlgorithm(properties.getAlgorithm());
        config.setKeyObtentionIterations(properties.getKeyObtentionIterations());
        config.setPoolSize(properties.getPoolSize());
        config.setProviderName(properties.getProviderName());
        config.setSaltGeneratorClassName(properties.getSaltGeneratorClassname());
        // org.jasypt.iv.NoIvGenerator
        config.setIvGeneratorClassName(properties.getIvGeneratorClassname());
        config.setStringOutputType(properties.getStringOutputType());
        encryptor.setConfig(config);
        return encryptor;
    }
}

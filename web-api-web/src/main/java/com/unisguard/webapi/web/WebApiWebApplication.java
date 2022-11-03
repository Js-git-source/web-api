package com.unisguard.webapi.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan({"com.unisguard.webapi.mapper"})
@SpringBootApplication(scanBasePackages = {"com.unisguard.webapi.web.config", "com.unisguard.webapi.controller", "com.unisguard.webapi.config.exception", "com.unisguard.webapi.service", "com.unisguard.webapi.manage"})
public class WebApiWebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApiWebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApiWebApplication.class, args);
    }
}

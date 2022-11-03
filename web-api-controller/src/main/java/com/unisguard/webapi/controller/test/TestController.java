package com.unisguard.webapi.controller.test;

import com.unisguard.webapi.common.util.IdGeneratorSnowflakeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping(value = "/snowflakeId")
    public long snowflakeId (){
        long id = IdGeneratorSnowflakeUtil.snowflakeId();
        System.out.println(id);
        return id;
    }
}

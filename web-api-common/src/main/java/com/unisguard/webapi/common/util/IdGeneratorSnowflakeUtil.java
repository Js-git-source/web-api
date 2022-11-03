package com.unisguard.webapi.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class IdGeneratorSnowflakeUtil {
    // 当前机器的workId
    private static long workId = 0;
    private static long datacenterId = 1;
    private static Snowflake snowflake = IdUtil.createSnowflake(workId, datacenterId);

    @PostConstruct
    public void init() {
        try {
            workId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workId: {}", workId);
        } catch (Exception e) {
            workId = NetUtil.getLocalhostStr().hashCode();
            log.warn("当前机器的workId获取失败", e);
        }
    }

    public static synchronized long snowflakeId(){
        return snowflake.nextId();
    }

    /**
     * 返回自定义值的全局唯一id号
     * @param workId 机房
     * @param datacenterId 第几台机器
     * @return id
     */
    public static synchronized long snowflakeId(long workId,long datacenterId){
        Snowflake snowflake = IdUtil.createSnowflake(workId,datacenterId);
        return snowflake.nextId();
    }
}

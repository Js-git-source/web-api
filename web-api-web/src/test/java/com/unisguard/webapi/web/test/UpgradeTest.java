package com.unisguard.webapi.web.test;

import com.alibaba.fastjson.JSONObject;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.util.ApiUtil;
import com.unisguard.webapi.common.util.HttpClientUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzemo
 * @date 2022/3/5 14:48
 */
class UpgradeTest {

    /**
     * 测试接口使用---在线升级调用接口测试
     */
    @Test
    void testUpgrade() {
        Map<String, String> header = new HashMap<>();
        Long clientSecret = System.currentTimeMillis();
        String clientTimeStamp = ApiUtil.createClientSecret("100414", "HZt5ceb^!X^bM7kAmR4Xoh#$LCDqL$FW", clientSecret);
        header.put("clientTimeStamp", clientSecret + "");
        header.put("clientSecret", clientTimeStamp);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", "/home/unisguard/upgrade/ngsoc-update-20220221.tar.gz");
        jsonObject.put("userId", "14");

        String resultMsg = new HttpClientUtil(600 * 1000, 600 * 1000).
                httpPostForHeaderAndBody("https://192.168.60.115:16500/unisguard/api/cli/mainUpgrade", jsonObject.toJSONString(), header, GlobalConstant.UTF_8);

        JSONObject object = JSONObject.parseObject(resultMsg);
        System.out.println("返回结果：" + object);


    }
}

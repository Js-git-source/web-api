package com.unisguard.webapi.service.upgrade.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.upgrade.UpgradeDO;
import com.unisguard.webapi.common.util.ApiUtil;
import com.unisguard.webapi.common.util.HttpClientUtil;
import com.unisguard.webapi.manage.upgrade.UpgradeManage;
import com.unisguard.webapi.service.upgrade.UpgradeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpgradeServiceImpl implements UpgradeService {
    private static final Logger log = LoggerFactory.getLogger(LogConstant.SYS);

    @Value("${upload.upgrade.path}")
    private String upgradePath;
    @Value("${upload.upgrade.api}")
    private String upgradeApi;

    @Resource
    private UpgradeManage upgradeManage;

    @Override
    public ResponseDO<List<UpgradeDO>> list(UpgradeDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        if (StringUtils.isNotBlank(param.getParam_time())) {
            String[] array = param.getParam_time().split("~");
            param.setStart_date(array[0] + " 00:00:00");
            if (array.length == 2) {
                param.setEnd_date(array[1] + " 23:59:59");
            }
        }
        List<UpgradeDO> upgradeDOList = upgradeManage.list(param);
        return ResponseDO.success(upgradeDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> upload(MultipartFile file, UpgradeDO param) throws Exception {
        // 获取导入的文件名
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return ResponseDO.failure("上传的文件名称不能为空");
        }
        if (!fileName.endsWith(".tar.gz")) {
            return ResponseDO.failure("上传的文件必须是.tar.gz类型");
        }
        File filePath = new File(upgradePath);
        // 如果文件夹不存在则创建，
        if (!filePath.exists()) {
            if (!filePath.mkdirs()) {
                return ResponseDO.failure("文件夹创建失败");
            }
        }
        File targetFile = new File(filePath, fileName);
        file.transferTo(targetFile);
        log.debug("在线升级，文件上传成功");
        // 上传成功之后直接调用接口
        String resultMsg = getResult(upgradePath + fileName, param.getUserId().toString());
        JSONObject object = JSONObject.parseObject(resultMsg);
        log.debug("在线升级，调用引擎，返回结果" + object);
        if (object == null) {
            return ResponseDO.failure("调用接口返回的结果为空");
        }
        if (object.get("status") != null && object.get("status").toString().equals("404")) {
            return ResponseDO.failure("调用接口不存在");
        }
        if (object.get("code") != null && "0".equals(object.get("code").toString())) {
            return ResponseDO.success();
        }
        String msg = object.get("msg") == null ? "" : object.get("msg").toString();
        return ResponseDO.failure(msg);
    }

    private String getResult(String path, String userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", path);
        jsonObject.put("userId", userId);
        log.debug("在线升级调用引擎接口，需要的参数" + jsonObject.toJSONString());
        Map<String, String> header = new HashMap<>();
        long clientSecret = System.currentTimeMillis();
        String clientTimeStamp = ApiUtil.createClientSecret("100414", "HZt5ceb^!X^bM7kAmR4Xoh#$LCDqL$FW", clientSecret);
        header.put("clientTimeStamp", clientSecret + "");
        header.put("clientSecret", clientTimeStamp);
        log.debug("在线升级调用引擎接口，需要的参数headMap" + JSONObject.toJSON(header).toString());
        return new HttpClientUtil(600 * 1000, 600 * 1000).
                httpPostForHeaderAndBody(upgradeApi, jsonObject.toJSONString(), header, GlobalConstant.UTF_8);
    }


}
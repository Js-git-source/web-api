package com.unisguard.webapi.service.lic.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import com.unisguard.webapi.common.dataobject.lic.ResponseAuthDO;
import com.unisguard.webapi.common.util.LicUtil;
import com.unisguard.webapi.common.util.LocalDateTimeUtil;
import com.unisguard.webapi.manage.lic.LicManage;
import com.unisguard.webapi.service.lic.LicService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LicServiceImpl implements LicService {
    @Resource
    private LicManage licManage;

    @Override
    public ResponseDO<List<LicDO>> list(LicDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<LicDO> licDOList = licManage.list(param);
        return ResponseDO.success(licDOList, page.getTotal());
    }

    @Override
    public ResponseDO<LicDO> last() {
        LicDO licDO = licManage.last();
        if (licDO == null) {
            return ResponseDO.success();
        }
        licDO.setRemainDay(LocalDateTimeUtil.getTimeDiff(LocalDateTime.now(), LocalDateTimeUtil.addDay(licDO.getAuthTime(), licDO.getAuthCycle())).intValue());
        if (new Integer(-1).equals(licDO.getAuthCycle())) {
            licDO.setRemainDay(-1);
        }
        return ResponseDO.success(licDO);
    }

    @Override
    public ResponseDO upload(MultipartFile file) throws Exception {
        String lic = IOUtils.toString(file.getInputStream(), Charset.defaultCharset());
        ResponseAuthDO responseAuthDO = LicUtil.checkLic(lic);
        LicDO licDO = new LicDO();
        licDO.setAuthName(responseAuthDO.getAuthName());
        licDO.setAuthTime(responseAuthDO.getAuthTime());
        licDO.setAuthCycle(responseAuthDO.getAuthCycle());
        licDO.setAuthContent(responseAuthDO.getAuthContent());
        licDO.setSecretKey(lic);
        licDO.setStatus(1);
        // 经度
        licDO.setLongitude(responseAuthDO.getLongitude());
        // 纬度
        licDO.setLatitude(responseAuthDO.getLatitude());
        // 国家(中文)
        licDO.setCountry(responseAuthDO.getCountry());
        // 国家(国家简码)
        licDO.setCountryCode(responseAuthDO.getCountryCode());
        // 区域/省(中文)
        licDO.setArea(responseAuthDO.getArea());
        // 城市(中文)
        licDO.setCity(responseAuthDO.getCity());
        licManage.add(licDO);
        GlobalConstant.LIC = responseAuthDO;
        return ResponseDO.success();
    }

    @Override
    public void download(HttpServletResponse response) throws Exception {
        String result = LicUtil.createRequestKey();
        response.setHeader("content-type", "text/html;charset=UTF-8");
        String filename = URLEncoder.encode("lic.req", "UTF-8");
        response.setHeader("Content-disposition", "attachment;fileName=" + filename);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}
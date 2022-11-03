package com.unisguard.webapi.service.exclude.impl;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.service.exclude.ExcludeService;
import com.unisguard.webapi.service.lic.LicService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Service
public class ExcludeServiceImpl implements ExcludeService {
    @Resource
    private LicService licService;

    @Override
    public ResponseDO upload(MultipartFile file) throws Exception {
        return licService.upload(file);
    }

    @Override
    public void download(HttpServletResponse response) throws Exception {
        licService.download(response);
    }
}
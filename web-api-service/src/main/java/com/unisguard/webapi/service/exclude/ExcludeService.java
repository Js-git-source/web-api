package com.unisguard.webapi.service.exclude;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ExcludeService {
    ResponseDO upload(MultipartFile file) throws Exception;

    void download(HttpServletResponse response) throws Exception;
}
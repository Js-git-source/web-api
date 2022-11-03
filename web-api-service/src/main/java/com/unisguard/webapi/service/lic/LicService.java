package com.unisguard.webapi.service.lic;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface LicService {
    ResponseDO<List<LicDO>> list(LicDO param);

    ResponseDO<LicDO> last();

    ResponseDO upload(MultipartFile file) throws Exception;

    void download(HttpServletResponse response) throws Exception;
}
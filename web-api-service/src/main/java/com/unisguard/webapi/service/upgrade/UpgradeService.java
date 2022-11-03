package com.unisguard.webapi.service.upgrade;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.upgrade.UpgradeDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UpgradeService {

    ResponseDO<List<UpgradeDO>> list(UpgradeDO param);

    ResponseDO<Long> upload(MultipartFile file, UpgradeDO param) throws Exception;


}
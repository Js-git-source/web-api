package com.unisguard.webapi.service.audit;

import com.unisguard.webapi.common.dataobject.audit.AuditDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;

import java.io.OutputStream;
import java.util.List;

public interface AuditService {


    ResponseDO<List<AuditDO>> list(AuditDO param);

    ResponseDO<AuditDO> detail(String esId);

    void add(AuditDO param) throws Exception;

    ResponseDO download(OutputStream outputStream, AuditDO param);

    ResponseDO clear(AuditDO param) throws Exception;


}

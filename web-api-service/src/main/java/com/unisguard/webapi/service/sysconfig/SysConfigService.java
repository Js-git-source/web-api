package com.unisguard.webapi.service.sysconfig;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigVO;
import org.hyperic.sigar.NetInterfaceConfig;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description 系统配置
 * @author sunsaike
 */
public interface SysConfigService {

    ResponseDO<List<SysConfigDO>> list(SysConfigDO param);

    ResponseDO<Long> add(SysConfigDO param);

    ResponseDO<SysConfigDO> detail(Long id);

    ResponseDO<Long> edit(SysConfigDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<List<NetInterfaceConfig>> network();

    ResponseDO<String> configDns(SysConfigVO param);
    /**
     * 网络测试
     * @param param 系统配置实体
     * @return 测试结果
     */
    ResponseDO<String> testNetwork(SysConfigVO param);

    ResponseDO<String> batchAdd(List<SysConfigDO> param);

    ResponseDO<Boolean> mailTest(SysConfigVO param);
    /**
     * 通过linux命令的方式获取当前系统时间
     * @return
     */
    ResponseDO<String> getSystemTime();
    /**
     * 周期同步
     * NTP周期校时 启用时：
     * 除了要保存 以下三个配置外，还要立即执行一次 ntpdate -u server 命令
     * NTP周期校时 禁用时：
     * 只保存 以下三个配置外
     * 1000609	NTP服务器
     * 1000610	NTP周期设置
     * 1000611	NTP周期校时
     */
    ResponseDO<String> syncTime(SysConfigVO param);
    /**
     * 立即同步
     * 除了要保存 以下三个配置外，还要立即执行一次 ntpdate -u server 命令
     * 1000609	NTP服务器
     * 1000610	NTP周期设置
     * 1000611	NTP周期校时
     */
    ResponseDO<String> rightNowSyncTime(SysConfigVO param);

    ResponseDO<String> uploadLogo(MultipartFile file, HttpServletRequest request,String logoPath);

    ResponseDO<String> loadLogo();

    void loadLogoDataStream(HttpServletRequest request,HttpServletResponse response);

    ResponseDO<List<SysConfigDO>> loadSysNameAndCopyRight();
}
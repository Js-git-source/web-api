package com.unisguard.webapi.service.sysconfig.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.sysconfig.NetConfig;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigVO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.exception.MessageCode;
import com.unisguard.webapi.common.util.*;
import com.unisguard.webapi.manage.sysconfig.SysConfigManage;
import com.unisguard.webapi.service.sysconfig.SysConfigService;
import org.apache.axis.encoding.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    private final static Log logger = LogFactory.getLog(LogConstant.SYS);
    @Resource
    private SysConfigManage sysConfigManage;

    @Override
    public ResponseDO<List<SysConfigDO>> list(SysConfigDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<SysConfigDO> sysConfigDOList = sysConfigManage.list(param);
        return ResponseDO.success(sysConfigDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(SysConfigDO param) {
        SysConfigDO sysConfigDO = new SysConfigDO();
        sysConfigDO.setDataKey(param.getDataKey());
        List<SysConfigDO> list = sysConfigManage.list(sysConfigDO);
        if (list != null && list.size() > 0) {
            SysConfigDO sysConfigDO1 = list.get(0);
            param.setId(sysConfigDO1.getId());
            sysConfigManage.edit(param);
        } else {
            sysConfigManage.add(param);
        }
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<SysConfigDO> detail(Long id) {
        SysConfigDO sysConfigDO = sysConfigManage.detail(id);
        return ResponseDO.success(sysConfigDO);
    }

    @Override
    public ResponseDO<Long> edit(SysConfigDO param) {
        sysConfigManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        sysConfigManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<List<NetInterfaceConfig>> network() {
        List<NetInterfaceConfig> networks = new ArrayList<>();
        try {
//            String cmd = "cat /etc/sysconfig/network-scripts/ifcfg-ens33 | grep GATEWAY | cut -d \"=\" -f 2";
//            String[] command = new String[]{"/bin/bash", "-c", cmd};
            Sigar sigar = SigarUtil.sigar;
            String[] names = sigar.getNetInterfaceList();
            for (String name : names) {
                if ("lo".equals(name) || "virbr0".equals(name)) {
                    continue;
                }
                NetConfig netConfig = new NetConfig();
                NetInterfaceConfig config = sigar.getNetInterfaceConfig(name);
                String cmd = "cat /etc/sysconfig/network-scripts/ifcfg-" + name + " | grep GATEWAY | cut -d \"=\" -f 2";
                String[] command = new String[]{"/bin/bash", "-c", cmd};
                String geteWay = executeCmd(command);
                geteWay = geteWay == null ? "" : geteWay.replaceAll("\r|\n", "");
                netConfig.setGateWay(geteWay);
                netConfig.setName(name);
                netConfig.setAddress(config.getAddress());
                netConfig.setNetmask(config.getNetmask());
                netConfig.setHwaddr(config.getHwaddr());
                networks.add(netConfig);
                logger.info("网络信息" + netConfig.toString());
                logger.info("------------------------------------------------------------------------");
            }
        } catch (Exception e) {
            logger.info("获取网卡信息异常", e);
        }
        return ResponseDO.success(networks);
    }

    @Override
    public ResponseDO<String> configDns(SysConfigVO param) {
        String firstDns = param.getFirstDns();
        if (!ValidatorUtil.isIPAddr(firstDns)) {
            throw new ApplicationException("首选DNS必须是IP");
        }
        String bakDns = param.getBakDns();
        if (StringUtils.isNotBlank(bakDns)) {
            if (!ValidatorUtil.isIPAddr(bakDns)) {
                throw new ApplicationException("备选DNS必须是IP");
            }
        }
        logger.info("firstDns=" + firstDns + " bakDns=" + bakDns);
        // 首选DNS 保存数据库
        SysConfigDO firstDnsDO = new SysConfigDO();
        firstDnsDO.setDataKey(DictConstant.PREFERRED_DNS);
        firstDnsDO.setDataValue(firstDns);
        add(firstDnsDO);
        // 备用DNS 保存数据库
        SysConfigDO bakDnsDO = new SysConfigDO();
        bakDnsDO.setDataKey(DictConstant.BFT_DNS);
        bakDnsDO.setDataValue(bakDns);
        add(bakDnsDO);
        String dnsSh = ResourceUtil.getRootPath();
        logger.info("dnsSh = " + dnsSh);
        String shPath = dnsSh + "config/configdns.sh";
        String[] params = new String[3];
        params[0] = shPath;
        params[1] = firstDns;
        params[2] = bakDns;
        String s = executeSh(params);
        return ResponseDO.success(s);
    }

    @Override
    public ResponseDO<String> testNetwork(SysConfigVO param) {
        String type = param.getType();
        String target = param.getTarget();
        String cmd = "";
        // ping测试
        if (type.equals(String.valueOf(DictConstant.NETWORK_PING))) {
            cmd = "ping -c 4 ";
        }
        // telnet测试
        else if (type.equals(String.valueOf(DictConstant.NETWORK_TELNET))) {
            cmd = "telnet ";
        }
        // traceroute
        else if (type.equals(String.valueOf(DictConstant.NETWORK_TRACEROUTE))) {
            cmd = "traceroute ";
        }
        String[] command = new String[]{"/bin/bash", "-c", cmd + target};
        String s = executeCmd(command);
        return ResponseDO.success(s);
    }

    @Override
    public ResponseDO<String> batchAdd(List<SysConfigDO> param) {
        for (SysConfigDO sysConfigDO : param) {
            if (!sysConfigDO.getDataKey().equals(DictConstant.MAIL_SENDER_ACCOUNT)
                    && !sysConfigDO.getDataKey().equals(DictConstant.MAIL_SENDER_PASSWORD)
                    && !sysConfigDO.getDataKey().equals(DictConstant.MAIL_RECEIVER_TEST)) {
                if (sysConfigDO.getDataKey() == null || sysConfigDO.getDataKey() == 0) {
                    throw new ApplicationException("配置标识不能为空");
                }
                if (StringUtils.isBlank(sysConfigDO.getDataValue())) {
                    throw new ApplicationException("配置内容不能为空");
                }
            }
            add(sysConfigDO);
        }
        SysConfigUtil.initFirstLogin(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Boolean> mailTest(SysConfigVO param) {
        try {
            param.setTopic("主题-测试");
            param.setFromPersonal("安全运营平台-公共模块");
            param.setContent("这是一个测试邮件");
            SendMailUtil.sendMail(param);
        } catch (Exception e) {
            logger.error("邮件测试异常", e);
            return ResponseDO.failure(MessageCode.EXECUTE_FAILURE, "测试失败！无法连接！！", false);
        }
        return ResponseDO.success(true);
    }

    @Override
    public ResponseDO<String> getSystemTime() {
        String dataStr;
        String[] command = new String[]{"/bin/bash", "-c", "date '+%Y-%m-%d %H:%M:%S'"};
        dataStr = executeCmd(command);
        if (dataStr.contains("\n")) {
            dataStr = dataStr.replaceAll("\n", "");
        }
        return ResponseDO.success(dataStr);
    }

    @Override
    public ResponseDO<String> syncTime(SysConfigVO param) {
        String result = syncCycle(param);
        logger.info("保存配置并且周期执行的结果为：" + result);
        return ResponseDO.success(result);
    }

    private String syncCycle(SysConfigVO param) {
        String ntpServer = param.getNtpServer();
        String cron = param.getCron();
        String cycleSwitch = param.getCycleSwitch();
        logger.info("ntpServer=" + ntpServer + " cron=" + cron + " cycleSwitch=" + cycleSwitch);
        // NTP服务器 保存数据库
        SysConfigDO ntpServerDO = new SysConfigDO();
        ntpServerDO.setDataKey(DictConstant.NTP_SERVER);
        ntpServerDO.setDataValue(ntpServer);
        add(ntpServerDO);
        // NTP周期设置 保存数据库
        SysConfigDO cronDO = new SysConfigDO();
        cronDO.setDataKey(DictConstant.NTP_CYCLE);
        cronDO.setDataValue(cron);
        add(cronDO);
        // NTP周期校时 保存数据库
        SysConfigDO cycleSwitchDO = new SysConfigDO();
        cycleSwitchDO.setDataKey(DictConstant.NTP_VERIFY);
        cycleSwitchDO.setDataValue(cycleSwitch);
        add(cycleSwitchDO);

        String ntpShPath = ResourceUtil.getRootPath();
        logger.info("ntpShPath = " + ntpShPath);
        String shPath = ntpShPath + "config/configntp.sh";
        String[] params = new String[4];
        params[0] = shPath;
        params[1] = cron;
        params[2] = ntpServer;
        params[3] = cycleSwitch;
        return executeSh(params);
    }

    @Override
    public ResponseDO<String> rightNowSyncTime(SysConfigVO param) {
        String ntpServer = param.getNtpServer();
        String ntpnowSh = ResourceUtil.getRootPath();
        logger.info("ntpnowSh = " + ntpnowSh);
        String shPath = ntpnowSh + "config/ntpnow.sh";
        String[] params = new String[2];
        params[0] = shPath;
        params[1] = ntpServer;
        String result = executeSh2(params);
        logger.info("@@@@@@@@@@@@@@:" + result);
        if ("1".equals(result)) {
            return ResponseDO.failure(-1, "立即同步失败", result);
        }
        if (result != null && result.length() > 19) {
            result = result.substring(1);
        }
        return ResponseDO.success(0, "立即同步成功", result);
    }

    @Override
    public ResponseDO<String> uploadLogo(MultipartFile file, HttpServletRequest request, String uploadLogoPath) {
        String s = "上传LOGO失败";
        try {
            s = uploadImage(file, uploadLogoPath);
        } catch (Exception e) {
            logger.error(s, e);
            return ResponseDO.failure(s);
        }
        // 将系统LOGO地址 保存数据库
        SysConfigDO sysConfigDO = new SysConfigDO();
        sysConfigDO.setDataKey(DictConstant.SYS_LOGO);
        sysConfigDO.setDataValue(uploadLogoPath + file.getOriginalFilename());
        add(sysConfigDO);
        return ResponseDO.success(s);
    }

    @Override
    public ResponseDO<String> loadLogo() {
        ByteArrayOutputStream stream = null;
        String base64 = null;
        try {
            SysConfigDO sysConfigDO = new SysConfigDO();
            sysConfigDO.setDataKey(DictConstant.SYS_LOGO);
            List<SysConfigDO> list = sysConfigManage.list(sysConfigDO);
            if (list.size() > 0) {
                SysConfigDO sysConfigDO1 = list.get(0);
                BufferedImage image = ImageIO.read(new File(sysConfigDO1.getDataValue()));
                //输出流
                stream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", stream);
                base64 = Base64.encode(stream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载图片异常", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseDO.success(base64);
    }

    @Override
    public void loadLogoDataStream(HttpServletRequest request, HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        OutputStream out = null;
        try {
            SysConfigDO sysConfigDO = new SysConfigDO();
            sysConfigDO.setDataKey(DictConstant.SYS_LOGO);
            List<SysConfigDO> list = sysConfigManage.list(sysConfigDO);
            File file = null;
            if (list.size() > 0) {
                SysConfigDO sysConfigDO1 = list.get(0);
                file = new File(sysConfigDO1.getDataValue());
                if (!file.exists()) {
                    String configPath = ResourceUtil.getRootPath();
                    String logoPath = configPath + "config/default_logo.png";
                    file = new File(logoPath);
                }
            }
            // 如果没有图片地址的配置，也去加载默认的logo
            else {
                String configPath = ResourceUtil.getRootPath();
                String logoPath = configPath + "config/default_logo.png";
                file = new File(logoPath);
            }

            fileInputStream = new FileInputStream(file);
            response.setContentType("image/x-icon");
            out = response.getOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = fileInputStream.read(buff, 0, 100)) > 0) {
                out.write(buff, 0, rc);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("加载图片异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResponseDO<List<SysConfigDO>> loadSysNameAndCopyRight() {
        List<SysConfigDO> sysConfigDOList = new ArrayList<>();
        try {
            // 初始化系统名称查询实体
            SysConfigDO sysConfigDO = new SysConfigDO();
            sysConfigDO.setDataKey(DictConstant.SYS_NAME);
            //查询系统名称
            List<SysConfigDO> systemNameList = sysConfigManage.list(sysConfigDO);
            //如果“系统名称”未查到，则加载系统默认的系统名称
            if (systemNameList == null || systemNameList.size() <= 0) {
                SysConfigDO defaultNameDO = new SysConfigDO();
                defaultNameDO.setDataKey(DictConstant.SYS_NAME);
                defaultNameDO.setDataValue(GlobalConstant.DEFAULT_SYSTEM_NAME);
                sysConfigDOList.add(defaultNameDO);
            }
            // 数据库有系统名称配置，则将其加到 sysConfigDOList
            else {
                sysConfigDOList.addAll(systemNameList);
            }
            // 初始化版权信息查询实体
            SysConfigDO sysConfigDO2 = new SysConfigDO();
            sysConfigDO2.setDataKey(DictConstant.SYS_COPYRIGHT);
            List<SysConfigDO> copyRightList = sysConfigManage.list(sysConfigDO2);
            //如果“系统名称”未查到，则加载系统默认的系统名称
            if (copyRightList == null || copyRightList.size() <= 0) {
                SysConfigDO defaultCopyRightDO = new SysConfigDO();
                defaultCopyRightDO.setDataKey(DictConstant.SYS_COPYRIGHT);
                defaultCopyRightDO.setDataValue(GlobalConstant.DEFAULT_COPYRIGHT);
                sysConfigDOList.add(defaultCopyRightDO);
            }
            // 数据库有版权信息配置，则将其加到 sysConfigDOList
            else {
                sysConfigDOList.addAll(copyRightList);
            }
        } catch (Exception e) {
            logger.error("加载图片和版权信息异常-loadSysNameAndCopyRight", e);
        }
        return ResponseDO.success(sysConfigDOList, (long) sysConfigDOList.size());
    }

    public String uploadImage(MultipartFile file, String uploadLogoPath) throws IOException {
        // 判断上传的文件是否为空
        if (file == null) {
            logger.error("file为空");
            return "图片不能为空";
        }
        // 文件路径
        String path;
        double fileSize = file.getSize();
        logger.info("图片的大小是" + fileSize);

        byte[] byteSize = file.getBytes();
        logger.info("图片的byte大小是" + byteSize.toString());
        // 文件原名称
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件原名称:" + fileName);
        boolean isFlag = false;
        for (String type : GlobalConstant.IMAGE_TYPES) {
            if (StringUtils.endsWithIgnoreCase(fileName, type)) {
                isFlag = true;
                break;
            }
        }
        if (isFlag) {
            // 判断路径是否存在，不存在则创建
            File uploadPath = new File(uploadLogoPath);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            // 设置存放图片文件的路径
            path = uploadLogoPath + fileName;
            logger.info("存放图片文件的路径:" + path);
            File logoFilePath = new File(path);
            // 转存文件到指定的路径
            file.transferTo(logoFilePath);
            logger.info("图片成功上传到指定目录下");
            return "图片成功上传到指定目录下";
        } else {
            logger.info("不是我们想要的文件类型,请按要求重新上传");
            return "图片类型不支持,请按要求重新上传";
        }
    }

    private String executeCmd(String[] command) {
        logger.info("command = " + command.toString());
        StringBuffer result = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(command);
            //等待命令执行完成
            process.waitFor(10, TimeUnit.SECONDS);
            int ptr = 0;
            BufferedInputStream rd = new BufferedInputStream(process.getInputStream());
            while ((ptr = rd.read()) != -1) {
                result.append((char) ptr);
            }
            int ptr2 = 0;
            logger.info("getErrorStream = " + process.getErrorStream());
            BufferedInputStream rderr = new BufferedInputStream(process.getErrorStream());
            while ((ptr2 = rderr.read()) != -1) {
                result.append((char) ptr2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("*****result******" + result);
        return result.toString();
    }

    private String executeSh(String[] params) {
        String result = "";
        InputStream in = null;
        BufferedReader read = null;
        try {
            String shPath = params[0];
            //先赋予执行权限
            String command1 = "chmod +x " + shPath;
            Runtime.getRuntime().exec(command1).waitFor();

            Process pro = Runtime.getRuntime().exec(params);
            int result2 = pro.waitFor();
            logger.info("result2=" + result2);

            in = pro.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = read.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
            logger.info("结果：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (read != null) {
                    read.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    private String executeSh2(String[] params) {
        String result = "";
        InputStream in = null;
        BufferedReader read = null;
        try {
            String shPath = params[0];
            //先赋予执行权限
            String command1 = "chmod +x " + shPath;
            Runtime.getRuntime().exec(command1).waitFor();

            Process pro = Runtime.getRuntime().exec(params);
            int result2 = pro.waitFor();
            logger.info("executeSh2:result2=" + result2);

            in = pro.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = read.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            logger.info("executeSh2:结果：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (read != null) {
                    read.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
}
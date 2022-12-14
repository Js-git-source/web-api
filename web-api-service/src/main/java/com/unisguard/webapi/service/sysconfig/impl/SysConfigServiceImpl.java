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
                logger.info("????????????" + netConfig.toString());
                logger.info("------------------------------------------------------------------------");
            }
        } catch (Exception e) {
            logger.info("????????????????????????", e);
        }
        return ResponseDO.success(networks);
    }

    @Override
    public ResponseDO<String> configDns(SysConfigVO param) {
        String firstDns = param.getFirstDns();
        if (!ValidatorUtil.isIPAddr(firstDns)) {
            throw new ApplicationException("??????DNS?????????IP");
        }
        String bakDns = param.getBakDns();
        if (StringUtils.isNotBlank(bakDns)) {
            if (!ValidatorUtil.isIPAddr(bakDns)) {
                throw new ApplicationException("??????DNS?????????IP");
            }
        }
        logger.info("firstDns=" + firstDns + " bakDns=" + bakDns);
        // ??????DNS ???????????????
        SysConfigDO firstDnsDO = new SysConfigDO();
        firstDnsDO.setDataKey(DictConstant.PREFERRED_DNS);
        firstDnsDO.setDataValue(firstDns);
        add(firstDnsDO);
        // ??????DNS ???????????????
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
        // ping??????
        if (type.equals(String.valueOf(DictConstant.NETWORK_PING))) {
            cmd = "ping -c 4 ";
        }
        // telnet??????
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
                    throw new ApplicationException("????????????????????????");
                }
                if (StringUtils.isBlank(sysConfigDO.getDataValue())) {
                    throw new ApplicationException("????????????????????????");
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
            param.setTopic("??????-??????");
            param.setFromPersonal("??????????????????-????????????");
            param.setContent("????????????????????????");
            SendMailUtil.sendMail(param);
        } catch (Exception e) {
            logger.error("??????????????????", e);
            return ResponseDO.failure(MessageCode.EXECUTE_FAILURE, "?????????????????????????????????", false);
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
        logger.info("?????????????????????????????????????????????" + result);
        return ResponseDO.success(result);
    }

    private String syncCycle(SysConfigVO param) {
        String ntpServer = param.getNtpServer();
        String cron = param.getCron();
        String cycleSwitch = param.getCycleSwitch();
        logger.info("ntpServer=" + ntpServer + " cron=" + cron + " cycleSwitch=" + cycleSwitch);
        // NTP????????? ???????????????
        SysConfigDO ntpServerDO = new SysConfigDO();
        ntpServerDO.setDataKey(DictConstant.NTP_SERVER);
        ntpServerDO.setDataValue(ntpServer);
        add(ntpServerDO);
        // NTP???????????? ???????????????
        SysConfigDO cronDO = new SysConfigDO();
        cronDO.setDataKey(DictConstant.NTP_CYCLE);
        cronDO.setDataValue(cron);
        add(cronDO);
        // NTP???????????? ???????????????
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
            return ResponseDO.failure(-1, "??????????????????", result);
        }
        if (result != null && result.length() > 19) {
            result = result.substring(1);
        }
        return ResponseDO.success(0, "??????????????????", result);
    }

    @Override
    public ResponseDO<String> uploadLogo(MultipartFile file, HttpServletRequest request, String uploadLogoPath) {
        String s = "??????LOGO??????";
        try {
            s = uploadImage(file, uploadLogoPath);
        } catch (Exception e) {
            logger.error(s, e);
            return ResponseDO.failure(s);
        }
        // ?????????LOGO?????? ???????????????
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
                //?????????
                stream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", stream);
                base64 = Base64.encode(stream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("??????????????????", e);
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
            // ?????????????????????????????????????????????????????????logo
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
            logger.error("??????????????????", e);
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
            // ?????????????????????????????????
            SysConfigDO sysConfigDO = new SysConfigDO();
            sysConfigDO.setDataKey(DictConstant.SYS_NAME);
            //??????????????????
            List<SysConfigDO> systemNameList = sysConfigManage.list(sysConfigDO);
            //????????????????????????????????????????????????????????????????????????
            if (systemNameList == null || systemNameList.size() <= 0) {
                SysConfigDO defaultNameDO = new SysConfigDO();
                defaultNameDO.setDataKey(DictConstant.SYS_NAME);
                defaultNameDO.setDataValue(GlobalConstant.DEFAULT_SYSTEM_NAME);
                sysConfigDOList.add(defaultNameDO);
            }
            // ???????????????????????????????????????????????? sysConfigDOList
            else {
                sysConfigDOList.addAll(systemNameList);
            }
            // ?????????????????????????????????
            SysConfigDO sysConfigDO2 = new SysConfigDO();
            sysConfigDO2.setDataKey(DictConstant.SYS_COPYRIGHT);
            List<SysConfigDO> copyRightList = sysConfigManage.list(sysConfigDO2);
            //????????????????????????????????????????????????????????????????????????
            if (copyRightList == null || copyRightList.size() <= 0) {
                SysConfigDO defaultCopyRightDO = new SysConfigDO();
                defaultCopyRightDO.setDataKey(DictConstant.SYS_COPYRIGHT);
                defaultCopyRightDO.setDataValue(GlobalConstant.DEFAULT_COPYRIGHT);
                sysConfigDOList.add(defaultCopyRightDO);
            }
            // ???????????????????????????????????????????????? sysConfigDOList
            else {
                sysConfigDOList.addAll(copyRightList);
            }
        } catch (Exception e) {
            logger.error("?????????????????????????????????-loadSysNameAndCopyRight", e);
        }
        return ResponseDO.success(sysConfigDOList, (long) sysConfigDOList.size());
    }

    public String uploadImage(MultipartFile file, String uploadLogoPath) throws IOException {
        // ?????????????????????????????????
        if (file == null) {
            logger.error("file??????");
            return "??????????????????";
        }
        // ????????????
        String path;
        double fileSize = file.getSize();
        logger.info("??????????????????" + fileSize);

        byte[] byteSize = file.getBytes();
        logger.info("?????????byte?????????" + byteSize.toString());
        // ???????????????
        String fileName = file.getOriginalFilename();
        logger.info("????????????????????????:" + fileName);
        boolean isFlag = false;
        for (String type : GlobalConstant.IMAGE_TYPES) {
            if (StringUtils.endsWithIgnoreCase(fileName, type)) {
                isFlag = true;
                break;
            }
        }
        if (isFlag) {
            // ?????????????????????????????????????????????
            File uploadPath = new File(uploadLogoPath);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            // ?????????????????????????????????
            path = uploadLogoPath + fileName;
            logger.info("???????????????????????????:" + path);
            File logoFilePath = new File(path);
            // ??????????????????????????????
            file.transferTo(logoFilePath);
            logger.info("????????????????????????????????????");
            return "????????????????????????????????????";
        } else {
            logger.info("?????????????????????????????????,????????????????????????");
            return "?????????????????????,????????????????????????";
        }
    }

    private String executeCmd(String[] command) {
        logger.info("command = " + command.toString());
        StringBuffer result = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(command);
            //????????????????????????
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
            //?????????????????????
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
            logger.info("?????????" + result);
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
            //?????????????????????
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
            logger.info("executeSh2:?????????" + result);
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
package com.unisguard.webapi.common.dataobject.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @description 系统配置VO类
 * @author sunsaike
 */
@Data
@ApiModel(value = "系统配置传参实体")
public class SysConfigVO {
    @ApiModelProperty(value = "网络测试类型", position = 10)
    @NotBlank(message = "网络测试类型不能为空", groups = {NetworkTest.class})
    private String type;

    @ApiModelProperty(value = "网络测试目标地址", position = 20)
    @NotBlank(message = "网络测试目标地址不能为空", groups = {NetworkTest.class})
    private String target;

    @ApiModelProperty(value = "首选DNS", position = 30)
    @NotBlank(message = "首选DNS不能为空", groups = {DnsConfig.class})
    private String firstDns;

    @ApiModelProperty(value = "备用DNS", position = 40)
    private String bakDns;

    @ApiModelProperty(value = "NTP服务器", position = 50)
    @NotBlank(message = "NTP服务器不能为空", groups = {Ntp.class,NowNtp.class})
    private String ntpServer;

    @ApiModelProperty(value = "周期设置", position = 60)
    @NotBlank(message = "周期设置不能为空", groups = {Ntp.class})
    private String cron;

    @ApiModelProperty(value = "周期校时", position = 70)
    @NotBlank(message = "周期校时不能为空", groups = {Ntp.class})
    private String cycleSwitch;

    public interface NetworkTest {
    }
    public interface Ntp {
    }
    public interface NowNtp {
    }
    public interface DnsConfig {
    }

    @ApiModelProperty(value = "登录账号", position = 80)
    @NotBlank(message = "登录账号不能为空", groups = {MailTest.class})
    private String fromEmailAccount;

    @ApiModelProperty(value = "登录密码", position = 90)
    @NotBlank(message = "登录密码不能为空", groups = {MailTest.class})
    private String fromEmailPassword;

    @ApiModelProperty(value = "SMTP地址", position = 100)
    @NotBlank(message = "SMTP地址不能为空", groups = {MailTest.class})
    private String fromEmailSmtpHost;

    @ApiModelProperty(value = "发件人邮箱地址", position = 110)
    @NotBlank(message = "发件人邮箱地址不能为空", groups = {MailTest.class})
    private String fromMailAddr;

    @ApiModelProperty(value = "收件人邮箱地址", position = 120)
    @NotBlank(message = "收件人邮箱地址不能为空", groups = {MailTest.class})
    private String receiveMailAddr;

    @ApiModelProperty(value = "加密方式", position = 130)
    @NotBlank(message = "加密方式不能为空", groups = {MailTest.class})
    private String encryptMode;

    @ApiModelProperty(value = "端口", position = 140)
    @NotBlank(message = "端口不能为空", groups = {MailTest.class})
    private String smtpPort;

    /**主题*/
    private String topic;
    /**发件人昵称*/
    private String fromPersonal;
    /**收件人昵称*/
    private String receivePersonal;
    /**正文*/
    private String content;

    public interface MailTest {
    }
}

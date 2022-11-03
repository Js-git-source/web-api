package com.unisguard.webapi.common.dataobject.lic;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "系统授权响应")
public class ResponseAuthDO {

	private String userName;
	private String userHome;
	private String userDir;
	private String osName;
	private String osArch;
	private String macAddr;
	private LocalDateTime createTime;
	private LocalDateTime authTime;
	private String authName;
	private Integer authCycle;
	private String authContent;
	private List<String> authList;
	private String authJson;
	private String secretKey;
    // 源IP纬度
    private String latitude;
    // 源IP经度
    private String longitude;
    // 源IP国家(中文)
    private String country;
    // 源IP国家(国家简码)
    private String countryCode;
    // 源IP城市(中文)
    private String city;
    // 源IP区域
    private String area;

}

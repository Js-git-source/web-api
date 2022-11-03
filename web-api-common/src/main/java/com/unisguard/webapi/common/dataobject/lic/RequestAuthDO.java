package com.unisguard.webapi.common.dataobject.lic;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "系统授权请求")
public class RequestAuthDO {

	private String userName;
	private String userHome;
	private String userDir;
	private String osName;
	private String osArch;
	private String macAddr;
	private LocalDateTime createTime;

}

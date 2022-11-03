package com.unisguard.webapi.common.dataobject.sysconfig;

import lombok.Data;
import org.hyperic.sigar.NetInterfaceConfig;

@Data
public class NetConfig extends NetInterfaceConfig {
    private String gateWay;
    private String name;
    private String netmask;
    private String address;
    private String hwaddr;
}

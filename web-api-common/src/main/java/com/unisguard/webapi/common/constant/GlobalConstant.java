package com.unisguard.webapi.common.constant;

import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.dataobject.lic.ResponseAuthDO;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zemel
 * @date 2022/1/4 17:33
 */
public class GlobalConstant {

    // 数据字典缓存
    public static final ConcurrentHashMap<Integer, List<DictDO>> DICT_MAP = new ConcurrentHashMap<>();

    // 当前用户
    public static final String CURRENT_USER = "currentUser";

    // 验证码
    public static final String AUTH_CODE = "authCode";

    public static ResponseAuthDO LIC;

    public static final String YEAR = "year";

    public static final String MONTH = "month";

    public static final List<String> MENU_LIST = new ArrayList<>();

    /**
     * --------------------------------------索引名称---------------------------------------
     */
    public static final String INDEX_SYS_AUDIT = "sys_audit";           //审计日志

    /**
     * --------------------------------------字符编码---------------------------------------
     */
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";

    /**
     * --------------------------------------系统模块类别名称---------------------------------------
     */
    public static final String MODULE_LOGIN_NAME = "系统登录";
    public static final String MODULE_LOGIN_OUT_NAME = "系统退出";
    public static final String MODULE_PERSON_CENTER_NAME = "个人中心";
    public static final String MODULE_USER_NAME = "用户管理";
    public static final String MODULE_ROLE_NAME = "角色管理";
    public static final String MODULE_MENU_NAME = "菜单管理";
    public static final String MODULE_DEPT_NAME = "部门管理";
    public static final String MODULE_USER_GROUP_NAME = "用户组管理";
    public static final String MODULE_API_NAME = "API管理";
    public static final String MODULE_API_CLIENT_NAME = "API客户端";
    public static final String MODULE_DICT_NAME = "数据字典";
    public static final String MODULE_SYSTEM_NAME = "系统配置";
    public static final String MODULE_AUDIT_NAME = "审计日志";
    public static final String MODULE_MONITOR_NAME = "系统监控";
    public static final String MODULE_GRANT_NAME = "系统授权";
    public static final String MODULE_UPGRADE_NAME = "系统升级";

    public static final Integer SIXTY = 60;
    public static final Integer HUNDRED = 100;
    public static final Integer THOUSAND = 1000;

    // 所有在线用户
    public static final List<HttpSession> SESSION_LIST = Collections.synchronizedList(new ArrayList<>());
    /**
     * 默认系统名称
     */
    public static final String DEFAULT_SYSTEM_NAME = "北斗网络安全运营平台";
    /**
     * 默认版权信息
     */
    public static final String DEFAULT_COPYRIGHT = "国舜科技股份有限公司";
    /**
     * 系统支持上传logo的格式
     */
    public static final String[] IMAGE_TYPES = {".bmp", ".jpg", ".jpeg", ".png"};

    public static final String VISIT_ES_ERROR = "查询ES失败";

    // 强制修改密码
    public static boolean FORCING_CHANGE_PASSWORD = false;

    // 登录失败次数
    public static Integer LOGIN_FAIL_COUNT = 5;

    // 登录失败锁定时间（毫秒）
    public static Long LOGIN_LOCK_TIME = 5 * 60 * 1000L;

    // 登录失败锁定时间描述（秒）
    public static String LOGIN_LOCK_TIME_DESC = "5分钟";
}

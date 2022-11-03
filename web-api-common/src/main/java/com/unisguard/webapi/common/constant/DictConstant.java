package com.unisguard.webapi.common.constant;

/**
 * 字典常量
 */
public class DictConstant {

    /***************** 000000-数据状态 ***********************/
    // 默认
    public static final Integer DEFAULT = 0;
    // 启用
    public static final Integer ENABLE = 1;
    // 禁用
    public static final Integer DISABLE = 2;
    // 删除（软）
    public static final Integer DELETE = 9;

    /***************** 1000000-全局布尔类型 ***********************/
    // 是
    public static final Integer TRUE = 1000001;
    // 否
    public static final Integer FALSE = 1000002;


    /***************** 1000100-部门关系人类型 ***********************/
    // 部门负责人
    public static final Integer DEPT_HEAD = 1000101;
    // 安全接口人
    public static final Integer SAFETY_INTERFACE = 1000102;


    /***************** 1000200-菜单类型 ***********************/
    // 目录
    public static final Integer CATALOG = 1000201;
    // 菜单
    public static final Integer MENU = 1000202;
    // 按钮
    public static final Integer BUTTON = 1000203;


    /***************** 1000300-账号类型 ***********************/
    // 正式员工
    public static final Integer REGULAR_EMPLOYEE = 1000301;
    // 外包员工
    public static final Integer OUTSOURCING_EMPLOYEE = 1000302;
    // 菜单
    public static final Integer MENU_TYPE = 1000302;


    /***************** 1000400-账号授权类型 ***********************/
    // 角色授权
    public static final Integer ROLE_AUTH = 1000401;
    // 用户组授权
    public static final Integer GROUP_AUTH = 1000402;


    /***************** 1000500-角色类型 ***********************/
    // 系统管理
    public static final Integer SYS_MANAGE = 1000501;
    // 系统审计
    public static final Integer SYS_AUDIT = 1000502;
    // 普通用户
    public static final Integer NORMAL_USER = 1000503;
    // 安全运营
    public static final Integer SAFE_OPERATION = 1000504;


    /***************** 1000600-系统配置字段 ***********************/
    // 首选DNS
    public static final Integer PREFERRED_DNS = 1000601;
    // 备用DNS
    public static final Integer BFT_DNS = 1000602;
    // SMTP地址
    public static final Integer SMTP_ADDRESS = 1000603;
    // 邮件服务端口
    public static final Integer MAIL_PORT = 1000604;
    // 邮件服务加密方式
    public static final Integer MAIL_ENCRYPT = 1000605;
    // 发件人邮箱
    public static final Integer MAIL_SENDER = 1000606;
    // 发件人账号
    public static final Integer MAIL_SENDER_ACCOUNT = 1000607;
    // 发件人密码
    public static final Integer MAIL_SENDER_PASSWORD = 1000608;
    // NTP服务器
    public static final Integer NTP_SERVER = 1000609;
    // NTP周期设置
    public static final Integer NTP_CYCLE = 1000610;
    // NTP周期校时
    public static final Integer NTP_VERIFY = 1000611;
    // 系统LOGO地址
    public static final Integer SYS_LOGO = 1000612;
    // 系统名称
    public static final Integer SYS_NAME = 1000613;
    // 系统版权信息
    public static final Integer SYS_COPYRIGHT = 1000614;
    // 密码组成
    public static final Integer PASSWORD_COMPOSITION = 1000615;
    // 是否校验弱口令
    public static final Integer WEAK_PASSWORD = 1000616;
    // 密码长度
    public static final Integer PASSWORD_LENGTH = 1000617;
    // 首次登陆强制修改密码
    public static final Integer FIRST_FORCE = 1000618;
    // 最大登录失败次数
    public static final Integer MAX_FAIL_COUNT = 1000619;
    // 锁定时间
    public static final Integer LOCK_TIME = 1000620;
    // 锁定时间单位
    public static final Integer LOCK_TIME_UNIT = 1000621;
    // 密码有效期
    public static final Integer PASSWORD_VALIDITY_PERIOD = 1000622;
    // 密码有效期单位
    public static final Integer PASSWORD_VALIDITY_UNIT = 1000623;
    // 是否禁用周期内密码
    public static final Integer DISABLE_PERIOD_PASSWORD = 1000624;
    // 禁用周期内密码周期
    public static final Integer PASSWORD_CYCLE_IN_DISABLE_CYCLE = 1000625;
    // 密码到期邮件提醒
    public static final Integer PASSWORD_EXPIRATION_REMIND = 1000626;
    // 测试收件人邮箱
    public static final Integer MAIL_RECEIVER_TEST = 1000627;


    /***************** 1000700-邮件服务加密方式 ***********************/
    // 明文
    public static final Integer MAIL_ENCRYPT_CLEAR = 1000701;
    // SSL
    public static final Integer MAIL_ENCRYPT_SSL = 1000702;
    // TLS
    public static final Integer MAIL_ENCRYPT_TLS = 1000703;

    /***************** 1000800-账号锁定周期 ***********************/
    // 秒
    public static final Integer LOCK_SECOND = 1000801;
    // 分钟
    public static final Integer LOCK_MINUTE = 1000802;
    // 小时
    public static final Integer LOCK_HOUR = 1000803;


    /***************** 1000900-密码有效周期 ***********************/
    // 天
    public static final Integer PASSWORD_VALIDITY_DAY = 1000901;
    // 月
    public static final Integer PASSWORD_VALIDITY_MONTH = 1000902;


    /***************** 1001000-系统操作结果 ***********************/
    // 成功
    public static final int OPT_RET_SUCCESS = 1001001;
    // 失败
    public static final int OPT_RET_FAIL = 1001002;


    /***************** 1001100-服务引擎类别 ***********************/
    // web服务
    public static final int ENGINE_TYPE_WEB = 1001101;
    // 定时服务
    public static final int ENGINE_TYPE_TIME_TASK = 1001102;


    /***************** 1001200-系统模块类别 ***********************/
    // 系统登录
    public static final int MODULE_LOGIN = 1001201;
    // 系统退出
    public static final int MODULE_LOGIN_OUT = 1001202;
    // 个人中心
    public static final int MODULE_PERSON_CENTER = 1001203;
    // 用户管理
    public static final int MODULE_USER = 1001204;
    // 角色管理
    public static final int MODULE_ROLE = 1001205;
    // 菜单管理
    public static final int MODULE_MENU = 1001206;
    // 部门管理
    public static final int MODULE_DEPT = 1001207;
    // 用户组管理
    public static final int MODULE_USER_GROUP = 1001208;
    // API管理
    public static final int MODULE_API = 1001209;
    // API客户端
    public static final int MODULE_API_CLIENT = 1001210;
    // 数据字典
    public static final int MODULE_DICT = 1001211;
    // 系统配置
    public static final int MODULE_SYSTEM = 1001212;
    // 审计日志
    public static final int MODULE_AUDIT = 1001213;
    // 系统监控
    public static final int MODULE_MONITOR = 1001214;
    // 系统授权
    public static final int MODULE_GRANT = 1001215;
    // 系统升级
    public static final int MODULE_UPGRADE = 1001216;


    /***************** 1001300-系统操作类型 ***********************/
    // 添加
    public static final int OPT_ADD = 1001301;
    // 修改
    public static final int OPT_EDIT = 1001302;
    // 删除
    public static final int OPT_DEL = 1001303;
    // 上传
    public static final int OPT_UPLOAD = 1001304;
    // 下载
    public static final int OPT_DOWNLOAD = 1001305;
    // 授权
    public static final int OPT_GRANT = 1001306;
    // 清空
    public static final int OPT_CLEAR = 1001307;


    /***************** 1001400-系统监控分类 ***********************/
    // 核心组件
    public static final Integer CORE_COMPONENT = 1001401;
    // 服务节点
    public static final Integer SERVICE_NODE = 1001402;
    // 引擎服务
    public static final Integer ENGINE_SERVICE = 1001403;


    /***************** 1001500-系统核心组件类别 ***********************/
    // Web中间件
    public static final Integer WEB_MIDDLEWARE = 1001501;
    // 数据库
    public static final Integer DATABASE = 1001502;
    // 索引引擎
    public static final Integer INDEX = 1001503;
    // 缓存服务
    public static final Integer CACHE = 1001504;
    // 消息中间件
    public static final Integer MIDDLEWARE = 1001505;


    /***************** 1001600-系统节点分类 ***********************/
    // 分析中心
    public static final Integer ANALYSIS_CENTER = 1001601;


    /***************** 1001700-系统监控指标 ***********************/
    // 系统监控指标CPU
    public static final Integer SYSTEM_MONITOR_CPU = 1001701;
    // 系统监控指标内存
    public static final Integer SYSTEM_MONITOR_RAM = 1001702;
    // 系统监控指标线程
    public static final Integer SYSTEM_MONITOR_THREAD = 1001703;
    // 系统监控指标连接
    public static final Integer SYSTEM_MONITOR_CONNECT = 1001704;
    // 系统监控指标吞吐量
    public static final Integer SYSTEM_MONITOR_THROUGHPUT = 1001705;
    // 系统监控指标I/O
    public static final Integer SYSTEM_MONITOR_IO = 1001706;
    // 系统监控指标网络
    public static final Integer SYSTEM_MONITOR_NETWORK = 1001707;
    // 系统监控指标索引
    public static final Integer SYSTEM_MONITOR_INDEX = 1001708;
    // 系统监控指标Key
    public static final Integer SYSTEM_MONITOR_KEY = 1001709;
    // 系统监控指标Topic
    public static final Integer SYSTEM_MONITOR_TOPIC = 1001710;


    /***************** 1001800-升级方式 ***********************/
    // 在线升级
    public static final int ON_LINE_UPGRADE = 1001801;
    // 离线升级
    public static final int OFF_LINE_UPGRADE = 1001802;


    /***************** 1001900-网络测试工具类型 ***********************/
    // Ping测试
    public static final int NETWORK_PING = 1001901;
    // Telnet测试
    public static final int NETWORK_TELNET = 1001902;
    // Traceroute测试
    public static final int NETWORK_TRACEROUTE = 1001903;


    /***************** 1002000-系统升级动作 ***********************/
    // 全部替换
    public static final int UPGRADE_ALL_INSTEAD = 1002001;
    // 文件替换
    public static final int UPGRADE_FILE_INSTEAD = 1002002;


    /***************** 1002100-系统升级结果 ***********************/
    // 成功
    public static final int UPGRADE_SUCCESS = 1002101;
    // 失败
    public static final int UPGRADE_FAIL = 1002102;
    // 未执行
    public static final int UPGRADE_PENDING = 1002103;
}

package com.unisguard.webapi.controller.aop;

import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.common.dataobject.audit.AuditDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.service.audit.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Date;

/**
 * 操作日志记录
 *
 * @author wanglinghui
 * @Date 2022-01-13
 */
@Aspect
@Component
public class LogAspect {

    private static Logger log = LoggerFactory.getLogger(LogConstant.SYS);

    @Resource
    private AuditService auditService;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.unisguard.webapi.common.annotation.LogAudit)")
    public void logPointCut() {

    }

    /**
     * 切入前执行
     *
     * @param joinPoint 切点
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("doBefore...");
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    private void handleLog(JoinPoint joinPoint, Exception e, Object jsonResult) {
        try {
            // 获得注解
            LogAudit controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 审计日志
            AuditDO operLog = new AuditDO();
            // 服务/引擎编号
            operLog.setEngineId(controllerLog.engineId());
            // 模块编号
            operLog.setModuleId(controllerLog.moduleId());
            // 当前登录的对象
            CurrentUserDO currentUserDO = CurrentUserUtil.get();
            if (currentUserDO != null) {
                // 操作用户编号
                operLog.setUserId(currentUserDO.getId().intValue());
                // 操作用户姓名
                operLog.setUserName(currentUserDO.getName());
                // 操作用户账号
                operLog.setUserAcc(currentUserDO.getAccount());
            }
            // 操作时间
            operLog.setOpTime(new Date());
            // 操作类型编号
            operLog.setOpType(controllerLog.opType());
            // 默认成功
            operLog.setOpRet(DictConstant.OPT_RET_SUCCESS);
            //操作结果编号
            if (e != null) {
                operLog.setMsg("异常");
                //出现异常的时候是失败
                operLog.setOpRet(DictConstant.OPT_RET_FAIL);
            } else {
                if (jsonResult != null) {
                    int code = ((ResponseDO) jsonResult).getCode();
                    // 如果code 不等于0 说明操作错误
                    if (code != 0) {
                        operLog.setOpRet(DictConstant.OPT_RET_FAIL);
                    }
                    //日志内容-目前是参数内容
                    operLog.setMsg(((ResponseDO) jsonResult).getMsg());
                } else {
                    // 如果调用的方法无返回值时，默认是成功的--比如导出
                    operLog.setMsg("该操作无返回值");
                }
            }
            operLog.setClientIp(getIPAddress());
            auditService.add(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private LogAudit getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(LogAudit.class);
        }
        return null;
    }

    /**
     * @return 客户端ip
     * @throws Exception 异常
     */
    private String getIPAddress() throws Exception {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        String clientIp = requestAttributes.getRequest().getRemoteAddr();
        if (clientIp.equals("127.0.0.1") || clientIp.equals("0:0:0:0:0:0:0:1")) {
            // 根据网卡取本机配置的IP
            InetAddress iNet = InetAddress.getLocalHost();
            if (iNet != null) {
                clientIp = iNet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (clientIp != null && clientIp.length() > 15) {
            if (clientIp.indexOf(",") > 0) {
                clientIp = clientIp.substring(0, clientIp.indexOf(","));
            }
        }
        return clientIp;
    }


}
